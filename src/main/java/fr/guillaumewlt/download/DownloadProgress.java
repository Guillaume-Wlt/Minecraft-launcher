package fr.guillaumewlt.download;

import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.utils.ConsoleUtils;

public class DownloadProgress {

    private long totalSize;
    private long bytesRead;

    public DownloadProgress(long totalSize) {
        if (totalSize <= 0) {
            ConsoleMessage.DOWNLOAD_PROGRESS_SIZE_ERR.throwException();
        }
        this.totalSize = totalSize;
        this.bytesRead = 0;
    }

    public void update(String filename, int chunkSize) {
        bytesRead += chunkSize;
        int percent = (int) (bytesRead / (totalSize / 100.0));
        percent = Math.min(percent, 100);

        int filled = percent / 5;
        int empty = 20 - filled;

        String bar = "█".repeat(filled) + "░".repeat(empty);
        ConsoleMessage.DOWNLOAD_PROGRESS_UPDATE_MESSAGE.outPrint(filename, bar, percent);
    }

    public void complete() {
        System.out.println();
        ConsoleUtils.out.println(""); // \n
    }
}
