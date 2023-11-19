package commands;

import receivers.UserReceiver;
import result.Result;

public class SaveUserCommand  extends Command<UserReceiver>  {

    public SaveUserCommand() throws Exception {
        super(UserReceiver.GetInstance());
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