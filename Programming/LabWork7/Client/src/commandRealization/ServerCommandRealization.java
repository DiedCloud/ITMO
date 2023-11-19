package commandRealization;

import common.descriptions.CommandDescription;
import modules.InteractiveMode;

import java.io.IOException;

public class ServerCommandRealization extends CommandRealization {

    @Override
    public void call() {
        if (commandDescription.getArguments() != null)
            interactiveMode.printToUser("Ввод аргументов для команды...");
        inputObjectArguments();
        interactiveMode.printToUser("Происходит выполнение команды " + commandDescription.getName());
        execution();
        interactiveMode.printToUser("Отправка запроса на сервер...");
        sendRequest();
        interactiveMode.printToUser("Получение ответа от сервера...");
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
                        commandDescription.getLoader()
                                .enterWithMessage("Enter arguments of command according to description:\n\""
                                        + loadDescription.getDescription() + "\":", loadDescription);
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
