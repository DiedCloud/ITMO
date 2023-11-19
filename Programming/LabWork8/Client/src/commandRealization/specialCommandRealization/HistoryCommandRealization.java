package commandRealization.specialCommandRealization;

import commandRealization.ClientCommandRealization;
import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import modules.InteractiveMode;
import result.Result;

public class HistoryCommandRealization extends ClientCommandRealization {

    protected InteractiveMode interactiveMode;

    public HistoryCommandRealization(InteractiveMode interactiveMode) {
        super(new CommandDescription("history", LocalizationKeys.HISTORY_COMMAND), interactiveMode);
        this.interactiveMode = interactiveMode;
    }

    @Override
    public void inputObjectArguments() {}

    @Override
    public void execution() {
        interactiveMode.history();
        commandResult = Result.success(interactiveMode.getHistory());
    }
}
