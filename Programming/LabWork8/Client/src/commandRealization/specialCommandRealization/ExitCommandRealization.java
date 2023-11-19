package commandRealization.specialCommandRealization;

import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import modules.InteractiveMode;

public class ExitCommandRealization extends SpecialCommandRealization {

    public ExitCommandRealization(InteractiveMode interactiveMode) {
        super(new CommandDescription("exit", LocalizationKeys.EXIT_COMMAND), interactiveMode);
    }

    @Override
    public void execution() {
        try {
            interactiveMode.sendCommandDescription(interactiveMode.getCommandDescription("exit"));
            interactiveMode.exit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
