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
            System.out.println(ConsoleMessage.REQUESTINFOS_USERNAME_INPUT_MESSAGE.getMessage());
            String username = context.getUsername();
            if (username == null || username.isEmpty()) {
                username = "Player";
            }
            System.out.println(ConsoleMessage.REQUESTINFOS_USERNAME_MESSAGE.format(username));
            System.out.println(ConsoleMessage.REQUESTINFOS_MINIMUM_RAM_INPUT_MESSAGE.getMessage());
            String minRam = context.getMinRam();
            if (minRam == null || minRam.isEmpty() || minRam.equals("512")) {
                minRam = "512";
                System.out.println(ConsoleMessage.REQUESTINFOS_MINIMUM_RAM_MESSAGE.format(minRam, "(default)"));
            } else {
                System.out.println(ConsoleMessage.REQUESTINFOS_MINIMUM_RAM_MESSAGE.format(minRam, ""));
            }
            System.out.println(ConsoleMessage.REQUESTINFOS_MAXIMUM_RAM_INPUT_MESSAGE.getMessage());
            String maxRam = context.getMaxRam();
            if (maxRam == null || maxRam.isEmpty() || maxRam.equals("2")) {
                maxRam = "2";
                System.out.println(ConsoleMessage.REQUESTINFOS_MAXIMUM_RAM_MESSAGE.format(maxRam, "(default)"));
            } else {
                System.out.println(ConsoleMessage.REQUESTINFOS_MAXIMUM_RAM_MESSAGE.format(maxRam, ""));
            }
            context.setUsername(username);
            context.setMinRam(minRam);
            context.setMaxRam(maxRam);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
