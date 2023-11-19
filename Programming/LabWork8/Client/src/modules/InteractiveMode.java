package modules;

import TextReceivers.LocalizedTextReceiver;
import TextReceivers.TextReceiver;
import UserInterface.ConsoleUI;
import UserInterface.graphics.GraphicUI;
import caller.Caller;
import commandRealization.ServerCommandRealization;
import commandRealization.specialCommandRealization.ExecuteScriptCommandRealization;
import commandRealization.specialCommandRealization.ExitCommandRealization;
import commandRealization.specialCommandRealization.HelpCommandRealization;
import commandRealization.specialCommandRealization.HistoryCommandRealization;
import common.Authorization;
import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import loaders.ConsoleLoader;
import result.Result;
import result.UpdateWarning;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.*;
import java.util.stream.Collectors;


public class InteractiveMode {
    private static InteractiveMode interactiveMode;
    private final CallableManager callableManager = new CallableManager();
    private final ResultManager resultManager;
    private Thread listeningThread;
    private LocalizedTextReceiver textReceiver;
    private final ConsoleLoader loader;
    private final ObjectSender objectSender;
    private final RequestHandler requestHandler;
    private final CommandDescription loginCommandDescription = new CommandDescription("login", LocalizationKeys.LOGIN_COMMAND,
            List.of(new LoadDescription<>(String.class),
                    new LoadDescription<>(String.class)
            ));
    private final CommandDescription registerCommandDescription = new CommandDescription("register", LocalizationKeys.REGISTER_COMMAND,
            List.of(new LoadDescription<>(String.class),
                    new LoadDescription<>(String.class)
            ));
    private boolean isAuthorized = false;
    private Authorization authorization;
    private Map<String, CommandDescription> commandDescriptionMap;
    private static final Map<String, CommandDescription> specialCommands = Map.of("help", new CommandDescription("help", LocalizationKeys.HELP_COMMAND),
            "history", new CommandDescription("history", LocalizationKeys.HISTORY_COMMAND),
            "execute_script", new CommandDescription("execute_script", LocalizationKeys.EXECUTE_SCRIPT_COMMAND),
            "exit", new CommandDescription("exit", LocalizationKeys.EXIT_COMMAND));
    private final ArrayDeque<String> history = new ArrayDeque<>();
    private Notifier notifier;

    private InteractiveMode(LocalizedTextReceiver textReceiver, ConsoleLoader loader, RequestHandler requestHandler, ObjectSender objectSender) {
        this.textReceiver = textReceiver;
        this.loader = loader;
        this.requestHandler = requestHandler;
        this.resultManager = new ResultManager(requestHandler);
        notifier = new Notifier();
        listeningThread = new Thread(() -> {
            resultManager.start(notifier);
        });
        listeningThread.start();
        this.objectSender = objectSender;
    }

    public static InteractiveMode getInstance(LocalizedTextReceiver textReceiver, ConsoleLoader loader, RequestHandler requestHandler, ObjectSender objectSender) {
        if (interactiveMode == null) {
            interactiveMode = new InteractiveMode(textReceiver, loader, requestHandler, objectSender);
            specialCommands.get("help").setCaller(new HelpCommandRealization(interactiveMode));
            specialCommands.get("history").setCaller(new HistoryCommandRealization(interactiveMode));
            specialCommands.get("exit").setCaller(new ExitCommandRealization(interactiveMode));
            specialCommands.get("execute_script").setCaller(new ExecuteScriptCommandRealization(interactiveMode));
            specialCommands.get("execute_script").setOneLineArguments(List.of(new LoadDescription<>(LocalizationKeys.PATH, String.class)));
        }
        return interactiveMode;
    }


    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    private Result<HashMap<String, CommandDescription>> loadCommandDescriptionMap() {
        try {
            Result<HashMap<String, CommandDescription>> commandDescriptionMap = (Result<HashMap<String, CommandDescription>>) getResultFromServer();
            if (!commandDescriptionMap.isSuccess()) {
                return commandDescriptionMap;
            }
            this.commandDescriptionMap = commandDescriptionMap.getValue().get()
                    .values()
                    .stream()
                    .peek(commandDescription -> {
                        if (specialCommands.containsKey(commandDescription.getName()))
                            commandDescription.setCaller(specialCommands.get(commandDescription.getName()).getCaller());
                        else
                            commandDescription.setCaller(new ServerCommandRealization(commandDescription, interactiveMode));
                    }).collect(Collectors.toMap(CommandDescription::getName, commandDescription -> commandDescription));
            return commandDescriptionMap;
        } catch (Exception e) {
            return Result.failure(e, LocalizationKeys.ERROR_SERVER_CONNECTION);
        }
    }

    public <T> T deserialize(DatagramPacket packet) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException("SERIALIZATION_ERROR");
        }
        try {
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public CommandDescription getCommandDescription(String s) throws NoSuchElementException {
        if (specialCommands.containsKey(s))
            return specialCommands.get(s);
        return commandDescriptionMap.get(s);
    }

    public Map<String, CommandDescription> getCommandDescriptionMap(){
        return commandDescriptionMap;
    }


    public void start() {
        //ConsoleUI.getInstance(this).start(); // для смены интерфейса требуется поменять эту строчку
        GraphicUI.getInstance(this).start(notifier);
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    public Result<?> register() {
        enterLoginData(registerCommandDescription);
        try {
            try {
                sendCommandDescription(registerCommandDescription);
            } catch (IOException ex) {
                throw new RuntimeException("");
            }
            Result<?> registerResult = getResultFromServer();
            if (registerResult.isSuccess()) {
                textReceiver.println(LocalizationKeys.REGISTER_SUCCESS);
                isAuthorized = true;
                return Result.success(null);
            } else {
                return registerResult;
            }
        } catch (Exception e) {
            return Result.failure(e, LocalizationKeys.ERROR_SERVER_CONNECTION);
        }
    }

    public Result<?> registerByText(String login, String password) {
        LoadDescription<String> loginDescription = new LoadDescription<>(String.class);
        loginDescription.setValue(login);
        LoadDescription<String> passwordDescription = new LoadDescription<>(String.class);
        passwordDescription.setValue(password);
        authorization = new Authorization(loginDescription.getValue(), passwordDescription.getValue());
        registerCommandDescription.setOneLineArguments(List.of(loginDescription, passwordDescription));
        try {
            try {
                sendCommandDescription(registerCommandDescription);
            } catch (IOException ex) {
                throw new RuntimeException("");
            }
            Result<?> registerResult = getResultFromServer();
            if (registerResult.isSuccess()) {
                textReceiver.println(LocalizationKeys.REGISTER_SUCCESS);
                isAuthorized = true;
                return Result.success(null);
            } else {
                return registerResult;
            }
        } catch (Exception e) {
            return Result.failure(e, LocalizationKeys.ERROR_SERVER_CONNECTION);
        }
    }

    private void enterLoginData(CommandDescription registerCommandDescription) {
        textReceiver.print(LocalizationKeys.ENTER_LOGIN);
        LoadDescription<String> loginDescription = loader.enterString(new LoadDescription<>(String.class));
        textReceiver.print(LocalizationKeys.ENTER_PASSWORD);
        LoadDescription<String> passwordDescription = loader.enterString(new LoadDescription<>(String.class));
        authorization = new Authorization(loginDescription.getValue(), passwordDescription.getValue());
        registerCommandDescription.setOneLineArguments(List.of(loginDescription, passwordDescription));
    }

    public Result<?> login() {
        enterLoginData(loginCommandDescription);
        try {
            sendCommandDescription(loginCommandDescription);
            Result<?> loginResult = loadCommandDescriptionMap();
            if (loginResult.isSuccess()) {
                textReceiver.println(LocalizationKeys.AUTH_SUCCESS);
                isAuthorized = true;
                return Result.success(null);
            } else {
                return loginResult;
            }
        } catch (Exception e) {
            return Result.failure(e, LocalizationKeys.ERROR_SERVER_CONNECTION);
        }
    }

    public Result<?> loginByText(String login, String password) {
        LoadDescription<String> loginDescription = new LoadDescription<>(String.class);
        loginDescription.setValue(login);
        LoadDescription<String> passwordDescription = new LoadDescription<>(String.class);
        passwordDescription.setValue(password);
        authorization = new Authorization(loginDescription.getValue(), passwordDescription.getValue());
        registerCommandDescription.setOneLineArguments(List.of(loginDescription, passwordDescription));
        try {
            sendCommandDescription(loginCommandDescription);
            Result<?> loginResult = loadCommandDescriptionMap();
            if (loginResult.isSuccess()) {
                isAuthorized = true;
                return Result.success(null);
            } else {
                return loginResult;
            }
        } catch (Exception e) {
            return Result.failure(e, LocalizationKeys.ERROR_SERVER_CONNECTION);
        }
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }


    public void history() {
        history.stream()
                .limit(6)
                .forEach(textReceiver::println);
    }

    public ArrayDeque<String> getHistory(){
        return history;
    }

    public void addCommandToHistory(String command) {
        history.add(command);
    }


    public Result<Void> exit() {
        try {
            requestHandler.closeChannel();
        } catch (IOException e) {
            textReceiver.println(LocalizationKeys.ERROR_CLOSING_CHANNEL);
        }
        isAuthorized = false;
        System.exit(0);
        return Result.success(null);
    }

    public void printHelp() {
        textReceiver.println(LocalizationKeys.AVAILABLE_COMMANDS);
        commandDescriptionMap.values()
                .forEach(commandDescription -> {
                    textReceiver.print(commandDescription.getName() + " - ");
                    textReceiver.println(commandDescription.getDescription());
                });
    }

    public void sendCommandDescription(CommandDescription commandDescription) throws IOException {
        commandDescription.setAuthorization(authorization);
        objectSender.sendObject(commandDescription);
    }

    public Result<?> getResultFromServer() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Result<?> result = resultManager.getResult();
        return result;
    }

    public void addCommandToQueue(Caller caller) {
        callableManager.add(caller);
    }

    public void clearCommandQueue() {
        callableManager.clear();
    }

    public List<Result<?>> executeAll() {
        return callableManager.callAll();
    }

    public void printToUser(Object o) {
        textReceiver.printAutoDetectType(o);
    }

    public void printToUser(LocalizationKeys o) {
        textReceiver.println(o);
    }

    public boolean isCommandExist(String s) {
        return commandDescriptionMap.containsKey(s);
    }

    public void setTextReceiver(LocalizedTextReceiver textReceiver) {
        this.textReceiver = textReceiver;
    }
}