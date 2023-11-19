package commands;

import receivers.MusicReceiver;
import result.Result;

/**
 * Class RemoveByIdCommand for removing an item from the collection by its id.
 */
public class RemoveByIdCommand extends Command<MusicReceiver>  {
    long id;

    /**
     * Constructor for creating a command object.
     */
    public RemoveByIdCommand(long id) throws Exception {
        super(MusicReceiver.GetInstance());
        this.id = id;
    }

    /**
     * Method execute calls the removeById() method of the receiver object.
     * @return result of executing the command (the result of the removeById() method of the receiver object)
     */
    @Override
    public Result<Void> execute() {
        return receiver.removeById(id);
    }
}