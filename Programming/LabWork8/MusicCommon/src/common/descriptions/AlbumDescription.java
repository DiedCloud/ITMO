package common.descriptions;

import common.LocalizationKeys;
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
                new LoadDescription<String>(LocalizationKeys.BEST_ALBUM_NAME, LocalizationKeys.BEST_ALBUM_NAME_FIELD, albumBuilder::setName, null, String.class),
                new LoadDescription<Long>(LocalizationKeys.LENGTH_OF_ALBUM, LocalizationKeys.LENGTH_OF_ALBUM_FIELD,  albumBuilder::setLength, null, Long.class),
                new LoadDescription<Long>(LocalizationKeys.NUMBER_OF_TRACKS, LocalizationKeys.NUMBER_OF_TRACKS_FIELD,  albumBuilder::setTracks, null, Long.class),
                new LoadDescription<Float>(LocalizationKeys.SALES, LocalizationKeys.SALES_FIELD,  albumBuilder::setSales, null, Float.class)));
    }

    public AlbumDescription(SerialFunction<Album, Object> fieldSetter) {
        super(LocalizationKeys.BEST_ALBUM, fieldSetter, Album.class);
    }

    public AlbumDescription() {
            super(LocalizationKeys.BEST_ALBUM, null, Album.class);
    }

}

