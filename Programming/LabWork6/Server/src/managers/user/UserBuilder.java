package managers.user;

import common.builders.Buildable;

import java.net.InetAddress;

public class UserBuilder implements Buildable<User> {
    long id;
    String login;
    String password;
    InetAddress host;
    int port;

    public UserBuilder setHost(InetAddress host) {
        this.host = host;
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

    public UserBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public User build() {
        return new User(login, password, host, port);
    }
}
