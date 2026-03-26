package fr.guillaumewlt.utils.console;

import lombok.Getter;

public enum ConsolePrefix {
    INFO("[INFO] "),
    INPUT("[INPUT] "),
    OUTPUT("[OUTPUT] "),
    ERROR("[ERROR] Error message >> "),
    FATAL_ERROR("[FATAL_ERROR] Stopping process >> ");

    @Getter
    private final String prefix;

    ConsolePrefix(String prefix) {
        this.prefix = prefix;
    }
}
