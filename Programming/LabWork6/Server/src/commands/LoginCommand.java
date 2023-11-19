package commands;

import receivers.UserReceiver;
import result.Result;

public class LoginCommand extends Command<UserReceiver> {
    String login;
    String password;
    public LoginCommand(String login, String password) throws Exception{
        super(UserReceiver.GetInstance());
        this.login = login;
        this.password = password;
    }

    @Override
    public Result<?> execute() {
        return receiver.check_login(login, password);
    }
}
