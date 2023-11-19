package commands;

import common.Collection;
import common.MusicBand;
import receivers.MusicReceiver;
import result.Result;

/**
 * Class ShowCommand for showing all elements of the collection.
 */
public class ShowCommand extends Command<MusicReceiver> {

    public ShowCommand() throws Exception {
        super(MusicReceiver.GetInstance());
    }

    /**
     * Method execute calls the showElementsOfCollection() method of the receiver object.
     * @return result of executing the command (the result of the showElementsOfCollection() method of the receiver object)
     */

    @Override
    public Result<java.util.Collection<MusicBand>> execute() {
        return receiver.getCollection();
    }
}