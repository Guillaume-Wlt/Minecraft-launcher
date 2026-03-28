package fr.guillaumewlt.processing;

import fr.guillaumewlt.utils.console.ConsoleMessage;

public class DownloadProgress {

    private long totalSize;
    private long bytesRead;
    private int lastMilestone;

    public DownloadProgress(long totalSize) {
        if (totalSize <= 0) {
            throw new IllegalArgumentException("totalSize must be grater than 0.");
        }
        this.totalSize = totalSize;
        this.bytesRead = 0;
        this.lastMilestone = -1;
    }

    public void update(String filename, int chunkSize) {
        bytesRead += chunkSize;
        int percent = (int) (bytesRead / (totalSize / 100.0));
        int milestone = (percent / 25) * 25;
        if (milestone != lastMilestone) {
            System.out.println(ConsoleMessage.DOWNLOAD_PROGRESS_UPDATE_MESSAGE.format(filename, milestone));
            lastMilestone = milestone;
        }
    }
}
