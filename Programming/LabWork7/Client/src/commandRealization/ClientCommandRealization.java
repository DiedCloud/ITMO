package commandRealization;

import common.descriptions.CommandDescription;
import modules.InteractiveMode;

public abstract class ClientCommandRealization extends CommandRealization {
    protected ClientCommandRealization(CommandDescription commandDescription, InteractiveMode interactiveMode) {
        super(commandDescription, interactiveMode);
    }

    @Override
    public void call() {
        if (commandDescription.getArguments() != null)
            interactiveMode.printToUser("Ввод аргументов для команды...");
        inputObjectArguments();
        interactiveMode.printToUser("Происходит выполнение команды " + commandDescription.getName());
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
