package commands;

import receivers.MusicReceiver;
import result.Result;

/**
 * Class SaveCommand for saving the collection to a file.
 */
public class SaveMusicCommand extends Command<MusicReceiver>  {

    public SaveMusicCommand() throws Exception {
        super(MusicReceiver.GetInstance());
    }

    /**
     * Method execute calls the saveCollection() method of the receiver object.
     * @return result of executing the command (the result of the saveCollection() method of the receiver object)
     */

    @Override
    public Result<Void> execute() {
        return receiver.saveCollection();
    }
}