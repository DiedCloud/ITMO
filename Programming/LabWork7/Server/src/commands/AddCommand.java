package commands;

import common.MusicBand;
import receivers.MusicReceiver;
import result.Result;

/**
 * Class AddCommand for adding a new element to the collection.
 */
public class AddCommand extends Command<MusicReceiver> {
    MusicBand element;

    /**
     * Constructor for creating a command object.
     * @param element
     */
    public AddCommand(MusicBand element) throws Exception {
        super(MusicReceiver.GetInstance());
        element.setID(MusicBand.getIdCounter());
        this.element = element;
    }

    /**
     * Method execute calls the add() method of the receiver object.
     * @return the result of executing the command (the result of the add() method of the receiver object)
     */
    @Override
    public Result<Void> execute() {
        return receiver.add(element);
    }
}