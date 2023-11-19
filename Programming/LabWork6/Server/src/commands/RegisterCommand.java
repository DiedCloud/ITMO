package commands;

import managers.user.User;
import receivers.UserReceiver;
import result.Result;

public class RegisterCommand extends Command<UserReceiver> {
    User user;
    public RegisterCommand(User user) throws Exception {
        super(UserReceiver.GetInstance());
        this.user = user;
    }

    @Override
    public Result<Void> execute() {
        return receiver.register(user);
    }
}
