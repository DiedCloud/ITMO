package commands;

import receivers.MusicReceiver;
import result.Result;

/**
 * Class RemoveByIdCommand for removing an item from the collection by its id.
 */
public class RemoveByIdCommand extends Command<MusicReceiver>  {
    long id;
    String ownerLogin;

    /**
     * Constructor for creating a command object.
     */
    public RemoveByIdCommand(long id, String ownerLogin) throws Exception {
        super(MusicReceiver.GetInstance());
        this.id = id;
        this.ownerLogin = ownerLogin;
    }

    /**
     * Method execute calls the removeById() method of the receiver object.
     * @return result of executing the command (the result of the removeById() method of the receiver object)
     */
    @Override
    public Result<Void> execute() {
        return ((MusicReceiver) receiver).removeById(id, ownerLogin);
    }
}