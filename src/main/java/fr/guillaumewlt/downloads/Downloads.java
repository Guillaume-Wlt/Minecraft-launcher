package fr.guillaumewlt.downloads;

import fr.guillaumewlt.utils.DirectoryPathUtils;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.URLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public abstract class Downloads {

    // Get Client Jar Name
    protected String clientJarName = LauncherUtils.getClientJarName();

    // Get Select Client jar (1.8.9, 1.21.5, ...) URL
    protected String selectedClientJarURL = URLUtils.getSelectedClientJarURL();

    // Get the launcher root directory
    protected String launcherDir = DirectoryPathUtils.getLauncherDir();

    protected abstract boolean download();

    protected abstract void checkRequirements();

    // Method to process the MD5 hash
    protected String computeMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Method to process the SHA-1 hash
    protected String computeSHA1(Path path) throws NoSuchAlgorithmException, IOException {
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
