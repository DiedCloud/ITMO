package result;

import common.MusicBand;

public class ClearWarning extends UpdateWarning{
    String ownerLogin;
    protected ClearWarning(String ownerLogin) {
        super(null, 0);
        this.ownerLogin = ownerLogin;
    }

    public static ClearWarning warning(String ownerLogin) {
        return new ClearWarning(ownerLogin);
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }
}
