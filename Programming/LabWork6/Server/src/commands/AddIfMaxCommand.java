package commands;

import common.MusicBand;
import receivers.*;
import result.Result;

/**
 * Class AddIfMaxCommand for adding a new element to the collection if it's greater than the maximum element.
 */
public class AddIfMaxCommand extends Command<MusicReceiver>   {

    MusicBand element;

    /**
     * Constructor for creating a command object.
     * @param element
     */
    public AddIfMaxCommand(MusicBand element) throws Exception {
        super(MusicReceiver.GetInstance());
        element.setID(MusicBand.getIdCounter());
        this.element = element;
    }

    /**
     * Method execute calls the addIfMax() method of the receiver object.
     * @return the result of executing the command (the result of the add() method of the receiver object)
     */
    @Override
    public Result<Void> execute() {
        return receiver.addIfMax(element);
    }
}