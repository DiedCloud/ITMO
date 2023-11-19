package commands;

import receivers.MusicReceiver;
import result.Result;

/**
 * Class ShowCommand for showing all elements of the collection.
 */
public class ExitCommand extends Command<MusicReceiver> {

    public ExitCommand() throws Exception {
        super(MusicReceiver.GetInstance());
    }

    /**
     * Method execute calls the showElementsOfCollection() method of the receiver object.
     * @return result of executing the command (the result of the showElementsOfCollection() method of the receiver object)
     */

    @Override
    public Result<Void> execute() {
        return Result.success(null);
    }
}