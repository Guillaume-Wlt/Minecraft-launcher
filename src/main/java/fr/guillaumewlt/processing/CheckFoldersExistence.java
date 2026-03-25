package fr.guillaumewlt.processing;

import fr.guillaumewlt.exceptionhandler.LauncherException;

import java.io.File;

public class CheckFoldersExistence {

    private CheckFoldersExistence() {}

    // Check for Folder Existence and create them if they are not already.
    public static void checkDirectories(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new LauncherException("Failed to create folder >> \"" + folder.getName() + "\", in >> " + folder.getPath());
            }
            System.out.println("Folder \"" + folder.getName() + "\" created in >> " + folder.getPath());
        } else {
            System.out.println("Folder \"" + folder.getName() + "\" already exists in >> " + folder.getPath());
        }
    }
}
