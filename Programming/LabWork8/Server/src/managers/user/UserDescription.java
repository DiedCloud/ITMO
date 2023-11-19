package managers.user;

import common.LocalizationKeys;
import common.descriptions.LoadDescription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class UserDescription extends LoadDescription<User> {

    public UserDescription(UserBuilder ub) {
        super(LocalizationKeys.USER, null, null, ub, User.class);
        builder = ub;
        fields = new ArrayList<>(Arrays.asList(
                new LoadDescription<String>(LocalizationKeys.LOGIN, LocalizationKeys.LOGIN, ub::setLogin, null, String.class),
                new LoadDescription<String>(LocalizationKeys.PASSWORD, LocalizationKeys.PASSWORD, ub::setPassword, null, String.class),
                new LoadDescription<String>(LocalizationKeys.SALT, LocalizationKeys.SALT, ub::setSalt, null, String.class)
        ));
    }
}
