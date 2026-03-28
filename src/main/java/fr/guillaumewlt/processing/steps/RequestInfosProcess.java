package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class RequestInfosProcess extends Processes{

    public RequestInfosProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            System.out.print("Enter your Username : ");
            String username = context.getScanner().nextLine();
            System.out.print("Enter the minimum of ram (in MegaBytes): ");
            String minRam = context.getScanner().nextLine();
            System.out.print("Enter the maximum of ram (in GigaBytes): ");
            String maxRam = context.getScanner().nextLine();
            context.setUsername(username);
            context.setMinRam(minRam);
            context.setMaxRam(maxRam);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
