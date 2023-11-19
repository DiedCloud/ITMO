package managers.connection;

import commands.LoginCommand;
import commands.RegisterCommand;
import common.Authorization;
import common.LocalizationKeys;
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

        commands.put("info", new CommandDescription("info", LocalizationKeys.INFO_COMMAND));
        commands.put("show", new CommandDescription("show", LocalizationKeys.SHOW_COMMAND));
        commands.put("clear", new CommandDescription("clear", LocalizationKeys.CLEAR_COMMAND));
        commands.put("help", new CommandDescription("help", LocalizationKeys.HELP_COMMAND));
        commands.put("exit", new CommandDescription("exit", LocalizationKeys.EXIT_COMMAND));
        commands.put("history", new CommandDescription("history", LocalizationKeys.HISTORY_COMMAND));
        commands.put("max_by_best_album", new CommandDescription("max_by_best_album", LocalizationKeys.MAX_BY_BEST_ALBUM_COMMAND));

        commands.put("add", new CommandDescription("add", LocalizationKeys.ADD_COMMAND, null, new ArrayList<>(List.of(new MusicBandDescription()))));
        commands.put("add_if_max", new CommandDescription("add_if_max", LocalizationKeys.ADD_IF_MAX_COMMAND, null, new ArrayList<>(List.of(new MusicBandDescription()))));
        commands.put("remove_greater", new CommandDescription("remove_greater", LocalizationKeys.REMOVE_GREATER_COMMAND, null, new ArrayList<>(List.of(new MusicBandDescription()))));

        commands.put("update", new CommandDescription("update", LocalizationKeys.UPDATE_COMMAND, new ArrayList<>(List.of(new LoadDescription<>(LocalizationKeys.ID, Long.class))), new ArrayList<>(List.of(new MusicBandDescription()))));

        commands.put("remove_by_id", new CommandDescription("remove_by_id", LocalizationKeys.REMOVE_COMMAND, new ArrayList<>(List.of(new LoadDescription<>(LocalizationKeys.ID, Long.class))), null));

        commands.put("execute_script", new CommandDescription("execute_script", LocalizationKeys.EXECUTE_SCRIPT_COMMAND, new ArrayList<>(List.of(new LoadDescription<>(LocalizationKeys.PATH, String.class))), null));

        commands.put("count_by_best_album", new CommandDescription("count_by_best_album", LocalizationKeys.COUNT_BY_BEST_ALBUM_COMMAND, null, new ArrayList<>(List.of(new AlbumDescription()))));

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
                RESULT = Result.failure(new Exception(""),LocalizationKeys.ERROR_WRONG_NUMBER_OF_ARGUMENTS);
                tmp_sender.addSending(() -> {
                    tmp_sender.send(RESULT);
                });
            }
            return;
        }
        if (!hasPermission(cd)) {
            Main.logger.error("Incorrect request: user didn't login");
            RESULT = Result.failure(new Exception(""), LocalizationKeys.ERROR_INCORRECT_LOGIN_OR_PASSWORD);
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
                Notifier.getInstance().addObserver(sender);
                Main.logger.info("New user connected");
                sender.addSending(() -> {
                    sender.send(Result.success(commands, LocalizationKeys.AUTH_SUCCESS));
                });
            }else{
                sender.addSending(() -> {
                    sender.send(Result.failure(new Exception("YOU_ARE_NOT_LOGGED_IN"), LocalizationKeys.ERROR_INCORRECT_LOGIN_OR_PASSWORD));
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
                sender.addSending(() -> {sender.send(Result.success(LocalizationKeys.REGISTER_SUCCESS));});
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
