package fr.guillaumewlt.exceptions;

public class LauncherException extends RuntimeException {

    public LauncherException(String message) {
        super(message);
    }
    public LauncherException(String message, Throwable cause) {
        super(message, cause);
    }
}
