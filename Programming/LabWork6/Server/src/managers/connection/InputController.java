package managers.connection;

import commands.HelpCommand;
import commands.LoginCommand;
import commands.RegisterCommand;
import common.descriptions.AlbumDescription;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import common.descriptions.MusicBandDescription;
import main.Main;
import managers.Invoker;
import managers.user.User;
import result.Result;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class InputController {

    private final int DISCONNECTING_TIMEOUT = 5;

    ResultSender rs = null;
    DatagramSocket ds;
    HashMap<String, CommandDescription> commands;

    private final Map<String, BiFunction<User, DatagramPacket, Void>> user_connection_commands;

    public InputController(DatagramSocket ds) {
        user_connection_commands = new HashMap<>();
        user_connection_commands.put("login", this::login);
        user_connection_commands.put("register", this::register);
        this.ds = ds;

        commands = new HashMap<>();

        commands.put("info", new CommandDescription("info", "Получить информацию о коллекции"));
        commands.put("show", new CommandDescription("show", "Получить элементы коллекции"));
        commands.put("clear", new CommandDescription("clear", "Очистить коллекцию"));
        commands.put("help", new CommandDescription("help", "Получить справочную информацию"));
        commands.put("exit", new CommandDescription("exit", "Выйти из приложения"));
        commands.put("history", new CommandDescription("history", "История введенных команд"));
        commands.put("max_by_best_album", new CommandDescription("max_by_best_album", "Получить MusicBand с наилучшим Album"));

        commands.put("add", new CommandDescription("add", "Добавить элемент в коллекцию", null, new ArrayList<>(List.of(new MusicBandDescription()))));
        commands.put("add_if_max", new CommandDescription("add_if_max", "Добавить элемент в коллекцию, проверив, что он больше уже имеющихся", null, new ArrayList<>(List.of(new MusicBandDescription()))));
        commands.put("remove_greater", new CommandDescription("remove_greater", "Удалить из коллекции все элементы, превышающие заданный", null, new ArrayList<>(List.of(new MusicBandDescription()))));

        commands.put("update", new CommandDescription("update", "Обновить элемент коллекции с указанным id", new ArrayList<>(List.of(new LoadDescription<>(Long.class))), new ArrayList<>(List.of(new MusicBandDescription()))));

        commands.put("remove_by_id", new CommandDescription("remove_by_id", "Удалить элемент с указанным id из коллекции", new ArrayList<>(List.of(new LoadDescription<>(Long.class))), null));

        commands.put("execute_script", new CommandDescription("execute_script", "Исполнить скрипт", new ArrayList<>(List.of(new LoadDescription<>(String.class))), null));

        commands.put("count_by_best_album", new CommandDescription("count_by_best_album", "Получить количество элементов, лучший Album которых соответствует заданному", null, new ArrayList<>(List.of(new AlbumDescription()))));

    }

    public void parse(CommandDescription cd, Invoker invoker, DatagramPacket dp) {
        Main.logger.info("Incoming request");
        Result<?> RESULT = null;
        if (user_connection_commands.containsKey(cd.getName())) {
            if (cd.getOneLineArguments().size() == 2)
                user_connection_commands.get(cd.getName()).apply(new User(
                        (String) cd.getOneLineArguments().get(0).getValue(),
                        (String) cd.getOneLineArguments().get(1).getValue(),
                        dp.getAddress(),
                        dp.getPort()
                ), dp);
            else {
                Main.logger.error("Incorrect request");
                ResultSender tmp_sender = new ResultSender(new User(null, null, dp.getAddress(), dp.getPort()), ds);
                RESULT = Result.failure(new Exception(""), "Ожидается ввод только 2 аргументов: логина и пароля");
                tmp_sender.send(RESULT);
            }
        } else {
            if (rs == null) {
                Main.logger.error("Incorrect request: user didn't login");
                RESULT = Result.failure(new Exception(""), "Войдите в систему");
                ResultSender tmp_sender = new ResultSender(new User(null, null, dp.getAddress(), dp.getPort()), ds);
                tmp_sender.send(RESULT);
            } else if (rs.user.getPort() == dp.getPort() && rs.user.getHost() == dp.getAddress()) {
                if (cd.getName().equals("exit")) {
                    close_client();
                } else {
                    RESULT = invoker.executeCommand(cd.getName(), cd);
                    rs.send(RESULT);
                }
            } else {
                RESULT = Result.failure(new Exception(""), "Занят занят работой с другим клиентом");
                ResultSender tmp_sender = new ResultSender(new User(null, null, dp.getAddress(), dp.getPort()), ds);
                tmp_sender.send(RESULT);
            }
        }
    }


    private Void login(User user, DatagramPacket dp) {
        ResultSender sender = new ResultSender(new User(null, null, dp.getAddress(), dp.getPort()), ds);
        try {
            if (rs == null || LocalDateTime.now().minusMinutes(DISCONNECTING_TIMEOUT).isAfter(rs.user.getLastActivity())) {
                if ((boolean) (new LoginCommand(user.getLogin(), user.getPassword()).execute().getValue().get())) {
                    if (rs != null) {
                        rs.send(Result.success(null, "Вы отключены от сервера, так как бездействовали больше " + DISCONNECTING_TIMEOUT + " минут и подключился другой пользователь."));
                        close_client();
                    }
                    rs = new ResultSender(user, ds);
                    Main.logger.info("New user connected");
                    rs.send(Result.success(commands, "Вход выполнен успешно"));
                } else {
                    sender.send(Result.failure(new Exception(), "Логин или пароль неверны"));
                }
            } else
                sender.send(Result.failure(new Exception(), "Сервер занят занят работой с другим клиентом"));
        } catch (Exception e) {
            Main.logger.error(e.getMessage(), e); //если по неизвестной причине UserReceiver, инициализированный в Main, при GetInstance кинет исключение
        }
        return null;
    }

    private Void register(User user, DatagramPacket dp) {
        ResultSender sender = new ResultSender(new User(null, null, dp.getAddress(), dp.getPort()), ds);
        try {
            Result<Void> r = new RegisterCommand(user).execute();
            if (r.isSuccess()) {
                Main.logger.info("New user registered");
                sender.send(Result.success(null, "Регистрация проведена. Теперь можно войти с этим же аккаунтом"));
            } else {
                Main.logger.error(r.getMessage());
                sender.send(Result.failure(r.getError().get(), r.getMessage()));
            }
        } catch (Exception e) {
            Main.logger.error(e.getMessage(), e);   //если по неизвестной причине UserReceiver, инициализированный в Main, при GetInstance кинет исключение
        }
        return null;
    }

    public void close_client() {
        Main.logger.info("User disconnected");
        rs = null;
    }
}
