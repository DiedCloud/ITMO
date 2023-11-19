package commands;

import main.Main;
import managers.user.User;
import receivers.MusicReceiver;
import receivers.UserReceiver;
import result.Result;

import java.util.concurrent.locks.ReentrantLock;

public class RegisterCommand extends Command<UserReceiver> {
    User user;
    public RegisterCommand(User user) throws Exception{
        super(UserReceiver.GetInstance());
        this.user = user;
    }

    @Override
    public Result<Void> execute() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            return receiver.register(user);
        } finally {
            lock.unlock();
        }
    }
}
