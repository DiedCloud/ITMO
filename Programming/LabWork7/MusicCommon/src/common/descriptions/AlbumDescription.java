package common.descriptions;

import common.builders.AlbumBuilder;
import common.Album;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class AlbumDescription extends LoadDescription<Album> implements Serializable {

    {
        AlbumBuilder albumBuilder = new AlbumBuilder();
        this.builder = albumBuilder;
        fields = new ArrayList<>(Arrays.asList(
                new LoadDescription<String>("Name", "BestAlbumName", albumBuilder::setName, null, String.class),
                new LoadDescription<Long>("Length of Album", "BestAlbumLength",  albumBuilder::setLength, null, Long.class),
                new LoadDescription<Long>("Number of tracks", "BestAlbumTracks",  albumBuilder::setTracks, null, Long.class),
                new LoadDescription<Float>("Sales", "BestAlbumSales",  albumBuilder::setSales, null, Float.class)));
    }

    public AlbumDescription(SerialFunction<Album, Object> fieldSetter) {
        super("The best album of Music Band", fieldSetter, Album.class);
    }

    public AlbumDescription() {
            super("The best album of Music Band", null, Album.class);
    }

}

