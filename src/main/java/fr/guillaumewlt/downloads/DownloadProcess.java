package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.DownloadProgress;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class DownloadProcess {

    public enum HashType {
        MD5, SHA1
    }

    private final LauncherContext context;

    public DownloadProcess(LauncherContext context) {
        this.context = context;
    }

    public void downloadToFile(String url, Path destination, long size, String name, String exceptedHash, HashType type) throws LauncherException {
        File localfile = destination.toFile();

        if (type == HashType.MD5) {
            try (InputStream is = URI.create(url).toURL().openStream()) {
                byte[] remoteContent = is.readAllBytes();
                String remoteHash = computeMD5(remoteContent);

                if (localfile.exists()) {
                    if(verify(localfile.toPath(), remoteHash, HashType.MD5)) return;
                }

                try (FileOutputStream fos = new FileOutputStream(localfile)) {
                    fos.write(remoteContent);
                    System.out.println("Download Success!");
                }
            } catch (IOException | NoSuchAlgorithmException ex) {
                throw new LauncherException(ex.getMessage());
            }
        } else if (type == HashType.SHA1) {
            try {
                if (size == 0) {
                    Files.createFile(destination);
                    return;
                }

                DownloadProgress downloadProgress = new DownloadProgress(size);

                if (localfile.exists()) {
                    if (verify(localfile.toPath(), exceptedHash, HashType.SHA1)) return;
                }

                try (InputStream is = URI.create(url).toURL().openStream();
                    OutputStream os = new FileOutputStream(localfile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                        downloadProgress.update(name, bytesRead);
                    }
                }
                downloadProgress.complete();

                if (!verify(destination, exceptedHash, HashType.SHA1)) {
                    Files.delete(destination);
                    throw new LauncherException("Download Failed!");
                } else {
                    System.out.println("Download Success!");
                }
            } catch (IOException ex) {
                throw new LauncherException(ex.getMessage());
            }
        }
    }

    public boolean verify(Path file, String exceptedHash, HashType type) {
        if (!Files.exists(file)) {
            throw new LauncherException(file + " does not exist");
        }

        String LocalFileHash;
        if (type == HashType.MD5) {
            try {
                byte[] fileBytes = Files.readAllBytes(file);
                LocalFileHash = computeMD5(fileBytes);
            } catch (IOException | NoSuchAlgorithmException ex) {
                throw new LauncherException(ex.getMessage());
            }
        } else if (type == HashType.SHA1) {
            try {
                LocalFileHash = computeSHA1(file);
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new LauncherException("Can't find good HashType");
        }
        return LocalFileHash.equals(exceptedHash);
    }

    // Method to process the MD5 hash
    private String computeMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Method to process the SHA-1 hash
    private String computeSHA1(Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        try (InputStream is = Files.newInputStream(path)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        return HexFormat.of().formatHex(md.digest());
    }
}
