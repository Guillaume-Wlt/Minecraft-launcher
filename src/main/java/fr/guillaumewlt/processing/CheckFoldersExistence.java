package fr.guillaumewlt.processing;

import fr.guillaumewlt.exceptionhandler.LauncherException;
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
                throw new LauncherException(ConsoleMessage.CHECK_FOLDER_EXISTENCE_ERR.format(folder.getName(), folder.getPath()));
            }
            System.out.println(ConsoleMessage.CHECK_FOLDER_EXISTENCE_CREATED.format(folder.getName(), folder.getPath()));
        } else {
            System.out.println(ConsoleMessage.CHECK_FOLDER_EXISTENCE_EXIST.format(folder.getName(), folder.getPath()));
        }
    }
}
