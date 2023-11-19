package result;

import common.LocalizationKeys;
import common.MusicBand;

public class UpdateWarning extends Result<MusicBand>{

    private long id;

    protected UpdateWarning(MusicBand value, long id) {
        super(true, LocalizationKeys.SUCCESS, value, null);
        this.id = id;
    }

    public static UpdateWarning warning(MusicBand value, long id) {
        return new UpdateWarning(value, id);
    }

    public long getId() {
        return id;
    }
}
