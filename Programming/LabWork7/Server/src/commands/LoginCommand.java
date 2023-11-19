package commands;

import receivers.UserReceiver;
import result.Result;

import java.util.concurrent.locks.ReentrantLock;

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
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            return receiver.check_login(login, password);
        }finally {
            lock.unlock();
        }
    }
}
