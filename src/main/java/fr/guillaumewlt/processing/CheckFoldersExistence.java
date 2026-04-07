package fr.guillaumewlt.processing;

import fr.guillaumewlt.utils.console.ConsoleMessage;

import java.io.File;

public class CheckFoldersExistence {

    private CheckFoldersExistence() {}

    // Check for Folder Existence and create them if they are not already.
    public static void checkDirectories(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                ConsoleMessage.CHECK_FOLDER_EXISTENCE_ERR.throwException(folder.getName(), folder.getPath());
            }
            ConsoleMessage.CHECK_FOLDER_EXISTENCE_CREATED.outPrintln(folder.getName(), folder.getPath());
        } else {
            ConsoleMessage.CHECK_FOLDER_EXISTENCE_EXIST.outPrintln(folder.getName(), folder.getPath());
        }
    }
}
