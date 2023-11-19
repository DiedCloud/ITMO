package commands;

import receivers.*;
import result.Result;

/**
 * Class ClearCommand for clearing the collection.
 */
public class ClearCommand extends Command<MusicReceiver> {
    public ClearCommand() throws Exception {
        super(MusicReceiver.GetInstance());
    }

    /**
     * Method for executing the command.
     * @return result for executing the command (the result of the clear() method of the receiver object)
     */
    @Override
    public Result<Void> execute() {
        return receiver.clear();
    }
}