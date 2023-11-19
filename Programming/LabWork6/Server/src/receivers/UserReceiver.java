package receivers;

import managers.file.AbstractFileReader;
import managers.file.AbstractFileWriter;
import managers.file.FileReader;
import managers.file.FileWriter;
import managers.file.decorators.CSV.CSVReader;
import managers.file.decorators.CSV.CSVWriter;
import common.Collection;
import managers.user.User;
import managers.user.UserBuilder;
import managers.user.UserDescription;
import result.Result;

public class UserReceiver extends Receiver<User>{
    private static UserReceiver instance;

    public static UserReceiver GetInstance() throws Exception{
        if (instance == null) {
            instance = new UserReceiver();
        }
        return instance;
    }

    private UserReceiver() throws Exception{

        String fileName = System.getenv("USERS_FILE");
        try {
            Collection<User> tmp = new Collection<>();
            AbstractFileWriter<User> Collection_to_file_writer = new FileWriter<>(fileName);
            AbstractFileReader<User> Collection_from_file_loader = new FileReader<>(fileName, new UserDescription(new UserBuilder()), tmp);

            Collection_to_file_writer = new CSVWriter<>(fileName, Collection_to_file_writer);
            Collection_from_file_loader = new CSVReader<>(fileName, new UserDescription(new UserBuilder()), Collection_from_file_loader, tmp);

            collection = new common.Collection<>(Collection_from_file_loader, Collection_to_file_writer);
        } catch (NullPointerException e){
            throw e;
        }
    }
    public Result<Boolean> check_login(String login, String password){
        return Result.success(
                collection.
                getCollection().
                stream().
                anyMatch((User user) -> (user.getLogin().equals(login) && user.getPassword().equals(password))));
    }

    public Result<Void> register(User user){
        if (collection.
                getCollection().
                stream().
                noneMatch((User u) -> (u.getLogin().equals(user.getLogin())))){
            return this.add(user);
        } else{
            return Result.failure(new Exception("Логин занят"), "Логин занят");
        }
    }

    @Override
    public Result<Collection<User>> showElementsOfCollection() {
        return Result.success(collection);
    }
}
