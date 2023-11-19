package commandRealization.specialCommandRealization;

import common.descriptions.CommandDescription;
import modules.InteractiveMode;

public class HelpCommandRealization extends SpecialCommandRealization {

    public HelpCommandRealization(InteractiveMode interactiveMode) {
        super(new CommandDescription("help","Вывод справки о командах"), interactiveMode);
    }

    @Override
    public void execution() {
        interactiveMode.printHelp();
    }
}
