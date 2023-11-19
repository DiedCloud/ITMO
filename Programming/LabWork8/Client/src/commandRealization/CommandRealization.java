package commandRealization;

import common.descriptions.CommandDescription;
import modules.InteractiveMode;
import result.Result;

public abstract class CommandRealization extends caller.Caller {

    protected Result<?> commandResult = Result.success(null);
    protected CommandDescription commandDescription;

    protected InteractiveMode interactiveMode;

    public CommandRealization(CommandDescription commandDescription, InteractiveMode interactiveMode) {
        this.commandDescription = commandDescription;
        this.interactiveMode = interactiveMode;
    }

    protected abstract void inputObjectArguments();

    protected abstract void execution();

    protected abstract void sendRequest();

    public void call() {
            inputObjectArguments();
            execution();
            sendRequest();
            getResultFromServer();
            printInfoForUser();
    }

    protected void printInfoForUser() {
         if(commandResult.isSuccess() && commandResult.getValue().isPresent()) {
             interactiveMode.printToUser(commandResult.getValue().get());
         }

         interactiveMode.printToUser(commandResult.getMessage());

         interactiveMode.printToUser(commandResult.getError().isPresent() ? commandResult.getError().get() : "");
    }

    protected void getResultFromServer(){
        commandResult = interactiveMode.getResultFromServer();
    }

    public Result<?> getResult(){
        return commandResult;
    }

    public CommandDescription getCommandDescription() {
        return commandDescription;
    }

    public void setCommandDescription(CommandDescription commandDescription) {
        this.commandDescription = commandDescription;
    }
}
