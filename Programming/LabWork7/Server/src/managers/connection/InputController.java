package managers.connection;

import commands.LoginCommand;
import commands.RegisterCommand;
import common.Authorization;
import common.descriptions.AlbumDescription;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import common.descriptions.MusicBandDescription;
import main.Main;
import managers.Invoker;
import managers.user.User;
import result.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class InputController {

    private final DatagramManager dm;
    HashMap<String, CommandDescription> commands;

    private final Map<String, Consumer<CommandDescription>> user_connection_commands = Map.of("login", this::login, "register", this::register);
    private static final ExecutorService parsingPool = Executors.newFixedThreadPool(100);

    public InputController(DatagramManager datagramManager) {

        this.dm = datagramManager;

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

    public void addParsing(Runnable task) {
        parsingPool.submit(task);
    }
    private boolean hasPermission(CommandDescription cd) {
        try {
            return (boolean) (new LoginCommand(cd.getAuthorization().getLogin(), cd.getAuthorization().getPassword()).execute().getValue().get());
        } catch (Exception e) {
            System.out.println(cd);
            e.printStackTrace();
            throw new RuntimeException("Error with login");
        }
    }

    public void parse(CommandDescription cd, Invoker invoker) {
        Main.logger.info("Incoming request from " + cd.getAuthorization().getLogin());
        Result<?> RESULT;
        ResultSender tmp_sender = new ResultSender(dm);
        if (user_connection_commands.containsKey(cd.getName())) {
            if (cd.getOneLineArguments().size() == 2)
                user_connection_commands.get(cd.getName()).accept(cd);
            else {
                Main.logger.error("Incorrect request");
                RESULT = Result.failure(new Exception(""), "Ожидается ввод только 2 аргументов: логина и пароля");
                tmp_sender.addSending(() -> {
                    tmp_sender.send(RESULT);
                });
            }
            return;
        }
        if (!hasPermission(cd)) {
            Main.logger.error("Incorrect request: user didn't login");
            RESULT = Result.failure(new Exception(""), "Неправильный логин или пароль");
        } else {
            Callable<Result<?>> execution = () -> invoker.executeCommand(cd.getName(), cd);
            Future<Result<?>> result = invoker.addExecution(execution);
            try {
                RESULT = result.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
        tmp_sender.addSending(() -> {
            tmp_sender.send(RESULT);
        });
    }



    private Void login(CommandDescription cd) {
        ResultSender sender = new ResultSender(dm);
        try {
            if(hasPermission(cd)) {
                Main.logger.info("New user connected");
                sender.addSending(() -> {
                    sender.send(Result.success(commands, "Вход выполнен успешно"));
                });
            }else{
                sender.addSending(() -> {
                    sender.send(Result.failure(new Exception("Авторизуйтесь"), "Неправильный логин или пароль"));
                });
            }
        } catch (Exception e) {
            Main.logger.error(e.getMessage(), e);
        }
        return null;
    }

    private Void register(CommandDescription cd) {
        User us = new User(
                (String) cd.getOneLineArguments().get(0).getValue(),
                (String) cd.getOneLineArguments().get(1).getValue(),
                dm
        );
        ResultSender sender = new ResultSender(new User(null, null, dm));
        try {
            Result<Void> r = new RegisterCommand(us).execute();
            if (r.isSuccess()) {
                Main.logger.info("New user registered");
                sender.addSending(() -> {sender.send(Result.failure(new Exception(), "Регистрация проведена. Теперь можно войти с этим же аккаунтом"));});
            } else {
                Main.logger.error(r.getMessage());
                sender.addSending(() -> {sender.send(Result.failure(r.getError().get(), r.getMessage()));});
            }
        } catch (Exception e) {
            Main.logger.error(e.getMessage(), e);   //если по неизвестной причине UserReceiver, инициализированный в Main, при GetInstance кинет исключение
        }
        return null;
    }

    public static void shutdownPool(){
        parsingPool.shutdown();
    }
}
