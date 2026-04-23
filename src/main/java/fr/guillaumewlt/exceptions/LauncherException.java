package fr.guillaumewlt.exceptions;

public class LauncherException extends RuntimeException {

    private final int exitCode;

    public LauncherException(String message) {
        super(message);
        exitCode = 1;
    }

    public LauncherException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public LauncherException(String message, Throwable cause) {
        super(message, cause);
        exitCode = 1;
    }

    public LauncherException(String message, Throwable cause, int exitCode) {
        super(message, cause);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
