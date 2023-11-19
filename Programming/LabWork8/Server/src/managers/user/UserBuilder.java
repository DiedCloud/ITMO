package managers.user;

import common.builders.Buildable;
import managers.connection.DatagramManager;

import java.net.InetAddress;

public class UserBuilder implements Buildable<User> {
    long id;
    String login;
    String password;
    String salt;
    DatagramManager dm;
    public UserBuilder setDM(DatagramManager dm) {
        this.dm = dm;
        return this;
    }

    public UserBuilder setID(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    @Override
    public User build() {
        return new User(login, password, salt, dm);
    }
}
