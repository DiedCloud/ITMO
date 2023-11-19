package managers.user;

import common.descriptions.LoadDescription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class UserDescription extends LoadDescription<User> {

    public UserDescription(UserBuilder ub) {
        super("User", null, null, ub, User.class);
        builder = ub;
        fields = new ArrayList<>(Arrays.asList(
                new LoadDescription<String>("login", "login", ub::setLogin, null, String.class),
                new LoadDescription<String>("password", "password", ub::setPassword, null, String.class),
                new LoadDescription<String>("salt", "salt", ub::setSalt, null, String.class)));
    }
}
