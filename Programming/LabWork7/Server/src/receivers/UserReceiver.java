package receivers;

import common.MusicBand;
import common.descriptions.LoadDescription;
import managers.file.*;
import common.Collection;
import managers.file.decorators.DataBase.DBReader;
import managers.file.decorators.DataBase.DBWriter;
import managers.user.User;
import managers.user.UserBuilder;
import managers.user.UserDescription;
import result.Result;

import java.util.concurrent.locks.ReentrantLock;

public class UserReceiver extends Receiver<User>{
    private static UserReceiver instance;

    public static UserReceiver GetInstance() throws Exception{
        if (instance == null) {
            instance = new UserReceiver();
        }
        return instance;
    }

    private UserReceiver() throws Exception{
            Collection<User> tmp = new Collection<>();
            collection_to_file_writer = new AbstractWriter<>("Users") {
                @Override
                public void write() throws Exception {
                    throw new Exception("Write method is not allowed here");
                }

                @Override
                public Result<Boolean> insert(User user) {
                    return null;
                }

                @Override
                public Result<Boolean> update(User user, long i) {
                    return null;
                }

                @Override
                public Result<Boolean> remove(long i) {
                    return null;
                }

                @Override
                public Result<Boolean> remove(String s, String s1) {
                    return null;
                }
            };
            AbstractReader<User> Collection_from_file_loader = new AbstractReader<>("Users", new UserDescription(new UserBuilder()), tmp) {
                @Override
                public Result<Collection<User>> read() {
                    return null;
                }
            };

            collection_to_file_writer = new DBWriter<>("Users", collection_to_file_writer, new UserDescription(new UserBuilder()));

            UserBuilder ub = new UserBuilder();
            UserDescription userDesc =new UserDescription(ub);
            userDesc.getFields().add(0, new LoadDescription<Long>("ID", "id", ub::setID, null, Long.class));
            Collection_from_file_loader = new DBReader<>("Users", userDesc, Collection_from_file_loader, tmp);

            collection = new common.Collection<>(Collection_from_file_loader, collection_to_file_writer);
    }
    public Result<Boolean> check_login(String login, String password){
            return Result.success(
                    collection.
                            getCollection().
                            stream().
                            anyMatch((User user) -> (user.getLogin().equals(login) && user.getHashedPassword(password).equals(user.getPassword()))));
    }

    public Result<Void> register(User user){
            if (collection.
                    getCollection().
                    stream().
                    noneMatch((User u) -> (u.getLogin().equals(user.getLogin())))) {
                return this.add(user);
            } else {
                return Result.failure(new Exception("Логин занят"), "Логин занят");
            }
    }

    @Override
    public Result<common.Collection<User>> showElementsOfCollection() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            return Result.success(collection);
        }finally {
            lock.unlock();
        }
    }
}
