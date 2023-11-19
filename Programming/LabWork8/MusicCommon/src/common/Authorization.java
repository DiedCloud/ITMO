package common;

import java.io.Serializable;

public class Authorization implements Serializable {
    protected String login;
    protected String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Authorization(String login, String password){
        this.login = login;
        this.password = password;
    }

}
