package fr.guillaumewlt.processing;

public class DownloadProgress {

    private long totalSize;
    private long bytesRead;

    public DownloadProgress(long totalSize) {
        if (totalSize <= 0) {
            throw new IllegalArgumentException("totalSize must be grater than 0.");
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
        System.out.print("\r" + filename + " [" + bar + "] " + percent + "%");

        if (percent >= 100) {
            System.out.println();
        }
    }
}
