package fr.guillaumewlt.utils;

import fr.guillaumewlt.console.ConsoleMessage;

import java.io.File;

public class DirectoryCreatorUtils {

    private DirectoryCreatorUtils() {}

    // Check for Folder Existence and create them if they are not already.
    public static void checkDirectories(String path) {
        File folder = new File(path);
        if (folder.exists()) {
            ConsoleMessage.CHECK_FOLDER_EXISTENCE_EXIST.outPrintln(folder.getName(), folder.getPath());
            return;
        }
        boolean created = folder.mkdirs();
        if (!created) {
            ConsoleMessage.CHECK_FOLDER_EXISTENCE_ERR.throwException(folder.getName(), folder.getPath());
            return;
        }
        ConsoleMessage.CHECK_FOLDER_EXISTENCE_CREATED.outPrintln(folder.getName(), folder.getPath());
    }
}
