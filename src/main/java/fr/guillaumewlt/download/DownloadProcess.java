package fr.guillaumewlt.download;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Handles file downloads with hash verification.
 * Supports MD5 and SHA-1 integrity checks to skip already valid files
 * and detect corrupted downloads.
 */
public class DownloadProcess {

    /**
     * Hash algorithm used for integrity verification.
     */
    public enum HashType {
        MD5, SHA1
    }

    private final LauncherContext context;

    public DownloadProcess(LauncherContext context) {
        this.context = context;
    }

    /**
     * Downloads a file from the given URL and verifies its integrity using MD5.
     * If the destination file already exists and its hash matches, the download is skipped.
     *
     * @param url         the URL to download from
     * @param destination the local path where the file will be saved
     * @throws LauncherException if the download or hash computation fails
     */
    public void downloadMD5(String url, Path destination) throws LauncherException {
        File localFile = destination.toFile();

        try (InputStream is = URI.create(url).toURL().openStream()) {
            byte[] remoteContent = is.readAllBytes();
            String remoteHash = computeMD5(remoteContent);

            if (localFile.exists()) {
                if(verify(localFile.toPath(), remoteHash, HashType.MD5)) return;
            }

            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(remoteContent);
                System.out.println("Download Success!");
            }
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new LauncherException(ex.getMessage());
        }
    }

    /**
     * Downloads a file from the given URL with progress tracking and verifies its integrity using SHA-1.
     * If the destination file already exists and its hash matches, the download is skipped.
     * If the file is empty (size == 0), an empty file is created without downloading.
     * If the downloaded file fails verification, it is deleted and an exception is thrown.
     *
     * @param url          the URL to download from
     * @param destination  the local path where the file will be saved
     * @param size         the expected file size in bytes (0 to create an empty file)
     * @param name         the display name used for progress reporting
     * @param expectedHash the expected SHA-1 hash of the file
     * @throws LauncherException if the download, verification, or file deletion fails
     */
    public void downloadSHA1(String url, Path destination, long size, String name, String expectedHash) throws LauncherException {
        File localFile = destination.toFile();

        try {
            if (size == 0) {
                Files.createFile(destination);
                return;
            }

            DownloadProgress downloadProgress = new DownloadProgress(size);

            if (localFile.exists()) {
                if (verify(localFile.toPath(), expectedHash, HashType.SHA1)) return;
            }

            try (InputStream is = URI.create(url).toURL().openStream();
                 OutputStream os = new FileOutputStream(localFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                    downloadProgress.update(name, bytesRead);
                }
            }
            downloadProgress.complete();

            if (!verify(destination, expectedHash, HashType.SHA1)) {
                Files.delete(destination);
                throw new LauncherException("Download Failed!");
            } else {
                System.out.println("Download Success!");
            }
        } catch (IOException ex) {
            throw new LauncherException(ex.getMessage());
        }
    }


    /**
     * Verifies the integrity of a local file by comparing its hash to an expected value.
     *
     * @param file         the path to the file to verify
     * @param expectedHash the expected hash string (hex-encoded)
     * @param type         the hash algorithm to use ({@link HashType#MD5} or {@link HashType#SHA1})
     * @return {@code true} if the file's hash matches the expected hash, {@code false} otherwise
     * @throws LauncherException if the file does not exist, cannot be read, or an unsupported hash type is given
     */
    public boolean verify(Path file, String expectedHash, HashType type) {
        if (!Files.exists(file)) {
            throw new LauncherException(file + " does not exist");
        }

        String localFileHash;
        if (type == HashType.MD5) {
            try {
                byte[] fileBytes = Files.readAllBytes(file);
                localFileHash = computeMD5(fileBytes);
            } catch (IOException | NoSuchAlgorithmException ex) {
                throw new LauncherException(ex.getMessage());
            }
        } else if (type == HashType.SHA1) {
            try {
                localFileHash = computeSHA1(file);
            } catch (IOException | NoSuchAlgorithmException ex) {
                throw new LauncherException(ex.getMessage());
            }
        } else {
            throw new LauncherException("Can't find good HashType");
        }
        return localFileHash.equals(expectedHash);
    }

    private String computeMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return HexFormat.of().formatHex(md.digest(data));
    }

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
