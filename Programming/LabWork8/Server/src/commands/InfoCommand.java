package commands;

import receivers.MusicReceiver;
import result.Result;

/**
 * Class InfoCommand for displaying information about the collection.
 */
public class InfoCommand extends Command<MusicReceiver> {

    public InfoCommand() throws Exception {
        super(MusicReceiver.GetInstance());
    }

    /**
     * Method execute calls the info() method of the receiver object.
     * @return result of executing the command (the result of the info() method of the receiver object)
     */
    @Override
    public Result<?> execute() {
        return receiver.info();
    }
}