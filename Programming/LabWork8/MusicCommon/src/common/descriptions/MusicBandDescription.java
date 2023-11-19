package common.descriptions;

import common.LocalizationKeys;
import common.builders.MusicBandBuilder;
import common.MusicBand;
import common.MusicGenre;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MusicBandDescription extends LoadDescription<MusicBand> implements Serializable {
    {
        MusicBandBuilder musicBandBuilder = new MusicBandBuilder();
        this.builder = musicBandBuilder;
        fields = new ArrayList<>(Arrays.asList(new LoadDescription<>(LocalizationKeys.NAME_OF_MUSICBAND, LocalizationKeys.NAME_OF_MUSICBAND_FIELD, musicBandBuilder::setName, null, String.class),
                new CoordinatesDescription(musicBandBuilder::setCoordinates),
                new LoadDescription<LocalDate>(LocalizationKeys.CREATION_DATE, LocalizationKeys.CREATION_DATE_FIELD, musicBandBuilder::setCreationDate, null, LocalDate.class),
                new LoadDescription<Long>(LocalizationKeys.NUMBER_OF_PARTICIPANTS, LocalizationKeys.NUMBER_OF_PARTICIPANTS_FIELD, musicBandBuilder::setNumberOfParticipants, null, Long.class),
                new AlbumDescription(musicBandBuilder::setBestAlbum),
                new LoadDescription<MusicGenre>(LocalizationKeys.GENRE_LIST, LocalizationKeys.GENRE_FIELD, musicBandBuilder::setGenre, null, MusicGenre.class)));
    }

    public MusicBandDescription(SerialFunction<MusicBand, Object> fieldSetter) {
        super(LocalizationKeys.MUSIC_BAND, fieldSetter, MusicBand.class);
    }

    public MusicBandDescription() {
        super(LocalizationKeys.MUSIC_BAND,
                (SerialFunction<MusicBand, ?>) null,
                MusicBand.class);
    }


}
