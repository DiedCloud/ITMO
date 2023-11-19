package commandRealization;

import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import modules.InteractiveMode;

public abstract class ClientCommandRealization extends CommandRealization {
    protected ClientCommandRealization(CommandDescription commandDescription, InteractiveMode interactiveMode) {
        super(commandDescription, interactiveMode);
    }

    @Override
    public void call() {
        if (commandDescription.getArguments() != null)
            interactiveMode.printToUser(String.valueOf(LocalizationKeys.ENTERING_ARGUMENTS_FOR_COMMAND));
        inputObjectArguments();
        interactiveMode.printToUser(LocalizationKeys.EXECUTING_COMMAND);
        execution();
        printInfoForUser();
    }

    @Override
    protected void sendRequest() {}

    @Override
    protected void getResultFromServer(){}

    @Override
    protected void printInfoForUser(){
        interactiveMode.printToUser(commandResult.getMessage());
    }
}
