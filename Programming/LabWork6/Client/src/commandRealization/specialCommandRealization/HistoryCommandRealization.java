package commandRealization.specialCommandRealization;

import commandRealization.ClientCommandRealization;
import common.descriptions.CommandDescription;
import modules.InteractiveMode;

public class HistoryCommandRealization extends ClientCommandRealization {

    protected InteractiveMode interactiveMode;

    public HistoryCommandRealization(InteractiveMode interactiveMode) {
        super(new CommandDescription("history","Вывод последних 6 команд"), interactiveMode);
        this.interactiveMode = interactiveMode;
    }

    @Override
    public void inputObjectArguments() {}

    @Override
    public void execution() {
        interactiveMode.history();
    }
}
