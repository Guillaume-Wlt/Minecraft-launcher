package fr.guillaumewlt.exceptionhandler;

public class FatalLauncherException extends RuntimeException {
    public FatalLauncherException(String message) {
        super(message);
    }

    public FatalLauncherException(String message, Throwable cause) {
        super(message, cause);
        System.exit(1);
    }
}
