package fr.guillaumewlt.utils.console;

public enum ConsolePrefix {
    INFO("[INFO] "),
    INPUT("[INPUT] "),
    OUTPUT("[OUTPUT] "),
    ERROR("[ERROR] Error message >> "),
    FATAL_ERROR("[FATAL_ERROR] Stopping process >> ");

    private final String label;

    ConsolePrefix(String label) {
        this.label = label;
    }

    public String getLabel() {
        String time = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "[" + time + "]" + label;
    }
}
