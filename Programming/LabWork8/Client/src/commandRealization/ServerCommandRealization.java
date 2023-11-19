package commandRealization;

import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import modules.InteractiveMode;

import java.io.IOException;

public class ServerCommandRealization extends CommandRealization {

    @Override
    public void call() {
        if (commandDescription.getArguments() != null)
            interactiveMode.printToUser(LocalizationKeys.ENTERING_ARGUMENTS_FOR_COMMAND);
        inputObjectArguments();
        interactiveMode.printToUser(LocalizationKeys.EXECUTING_COMMAND);
        execution();
        interactiveMode.printToUser(LocalizationKeys.REQUEST_SENDING);
        sendRequest();
        interactiveMode.printToUser(LocalizationKeys.REQUEST_RECEIVING);
        getResultFromServer();
        printInfoForUser();
    }
    public ServerCommandRealization(CommandDescription commandDescription, InteractiveMode interactiveMode) {
        super(commandDescription, interactiveMode);
    }

    @Override
    protected void inputObjectArguments() {
        if(commandDescription.getArguments() == null) {
            return;
        }
            commandDescription.getArguments()
                    .forEach(loadDescription -> {
                        interactiveMode.printToUser(LocalizationKeys.ENTER_ARGUMENTS_FROM_DESCRIPTION);
                        interactiveMode.printToUser(loadDescription.getDescription());
                        commandDescription.getLoader().enter(loadDescription);
                    });

    }

    @Override
    protected void execution() {}

    @Override
    protected void sendRequest(){
        try {
            interactiveMode.sendCommandDescription(commandDescription);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
