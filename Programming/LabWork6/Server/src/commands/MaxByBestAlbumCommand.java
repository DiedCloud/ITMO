package commands;

import common.MusicBand;
import receivers.MusicReceiver;
import result.Result;

/**
 * Class MaxByBestAlbumCommand for displaying the element with the maximum value of the bestAlbum field.
 */
public class MaxByBestAlbumCommand extends Command<MusicReceiver> {

    public MaxByBestAlbumCommand() throws Exception {
        super(MusicReceiver.GetInstance());
    }

    /**
     * Method execute calls the maxByBestAlbum() method of the receiver object.
     * @return result of executing the command (the result of the maxByBestAlbum() method of the receiver object)
     */
    @Override
    public Result<MusicBand> execute() {
        return receiver.maxByBestAlbum();
    }
}