package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

public class RequestInfosProcess extends Processes{

    public RequestInfosProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            ConsoleMessage.REQUESTINFOS_USERNAME_INPUT_MESSAGE.outPrintln();
            String username = context.getUsername();
            if (username == null || username.isEmpty()) {
                username = "Player";
            }
            ConsoleMessage.REQUESTINFOS_USERNAME_MESSAGE.outPrintln(username);
            ConsoleMessage.REQUESTINFOS_MINIMUM_RAM_INPUT_MESSAGE.outPrintln();
            String minRam = context.getMinRam();
            if (minRam == null || minRam.isEmpty() || minRam.equals("512")) {
                minRam = "512";
                ConsoleMessage.REQUESTINFOS_MINIMUM_RAM_MESSAGE.outPrintln(minRam, "(default)");
            } else {
                ConsoleMessage.REQUESTINFOS_MINIMUM_RAM_MESSAGE.outPrintln(minRam, "");
            }
            ConsoleMessage.REQUESTINFOS_MAXIMUM_RAM_INPUT_MESSAGE.outPrintln();
            String maxRam = context.getMaxRam();
            if (maxRam == null || maxRam.isEmpty() || maxRam.equals("2")) {
                maxRam = "2";
                ConsoleMessage.REQUESTINFOS_MAXIMUM_RAM_MESSAGE.outPrintln(maxRam, "(default)");
            } else {
                ConsoleMessage.REQUESTINFOS_MAXIMUM_RAM_MESSAGE.outPrintln(maxRam, "");
            }
            context.setUsername(username);
            context.setMinRam(minRam);
            context.setMaxRam(maxRam);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
