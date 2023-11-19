package commands;

import common.Album;
import common.MusicBand;
import receivers.MusicReceiver;
import result.Result;

import java.util.TreeSet;

/**
 * Class FilterByBestAlbum for filtering the collection by the bestAlbum field.
 */
public class FilterByBestAlbum extends Command<MusicReceiver>{
    Album album;

    /**
     * Constructor for creating a command object.
     * @param album bestAlbum
     */
    public FilterByBestAlbum(Album album) throws Exception {
        super(MusicReceiver.GetInstance());
        this.album = album;
    }

    /**
     * Method execute calls the filterByBestAlbum() method of the receiver object.
     * @return result of executing the command (the result of the filterByBestAlbum() method of the receiver object)
     */
    @Override
    public Result<TreeSet<MusicBand>> execute() {
        return receiver.filterByBestAlbum(album);
    }
}