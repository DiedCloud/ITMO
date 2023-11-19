package commandRealization.specialCommandRealization;

import commandRealization.ClientCommandRealization;
import common.descriptions.CommandDescription;
import modules.InteractiveMode;

public abstract class SpecialCommandRealization extends ClientCommandRealization {
    public SpecialCommandRealization(CommandDescription commandDescription, InteractiveMode interactiveMode) {
        super(commandDescription, interactiveMode);
    }

    @Override
    public void inputObjectArguments() {}


}
