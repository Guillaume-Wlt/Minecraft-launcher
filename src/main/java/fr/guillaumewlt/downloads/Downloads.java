package fr.guillaumewlt.downloads;

import fr.guillaumewlt.utils.LauncherUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Downloads {

    protected String launcherDir = LauncherUtils.getLauncherDir();

    protected abstract boolean download();

    protected abstract void checkRequirements();

    protected String computeMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
