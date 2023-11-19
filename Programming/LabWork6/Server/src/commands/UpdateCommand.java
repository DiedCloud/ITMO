package commands;

import common.MusicBand;
import receivers.MusicReceiver;
import result.Result;

/**
 * Class UpdateCommand for updating the value of the collection element whose id is equal to the specified one.
 */

public class UpdateCommand extends Command<MusicReceiver> {
    long id;
    MusicBand obj;

    /**
     * Constructor for creating a command object.
     */
    public UpdateCommand(long id, MusicBand obj) throws Exception{
        super(MusicReceiver.GetInstance());
        obj.setID(MusicBand.getIdCounter());
        this.id = id;
        this.obj = obj;
    }

    /**
     * Method execute calls the updateById() method of the receiver object.
     * @return result of executing the command (the result of the updateById() method of the receiver object)
     */
    public Result<Void> execute() {
       return receiver.updateById(id, obj);
    }
}