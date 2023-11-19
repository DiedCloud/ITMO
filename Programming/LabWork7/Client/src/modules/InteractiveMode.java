package modules;

import caller.Caller;
import commandRealization.ServerCommandRealization;
import commandRealization.specialCommandRealization.ExecuteScriptCommandRealization;
import commandRealization.specialCommandRealization.ExitCommandRealization;
import commandRealization.specialCommandRealization.HelpCommandRealization;
import commandRealization.specialCommandRealization.HistoryCommandRealization;
import common.Authorization;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import loaders.ConsoleLoader;
import result.Result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class InteractiveMode {
    private static InteractiveMode interactiveMode;
    private final CallableManager callableManager = new CallableManager();
    private final TextReceiver textReceiver;
    private final ConsoleLoader loader;
    private final ObjectSender objectSender;
    private final RequestHandler requestHandler;
    private final CommandDescription loginCommandDescription = new CommandDescription("login", "Вход пользователя",
            List.of(new LoadDescription<>(String.class),
                    new LoadDescription<>(String.class)
            ));
    private final CommandDescription registerCommandDescription = new CommandDescription("register", "Регистрация пользователя",
            List.of(new LoadDescription<>(String.class),
                    new LoadDescription<>(String.class)
            ));
    private boolean isAuthorized = false;

    private Authorization authorization;
    private Map<String, CommandDescription> commandDescriptionMap;
    private final Map<String, Supplier<Result<?>>> authorizationMap = Map.of("l", this::login,
            "r", this::register,
            "q", this::exit
    );
    private static final Map<String, CommandDescription> specialCommands = Map.of("help", new CommandDescription("help","Вывод справки о командах"),
            "history", new CommandDescription("history","Вывод последних 6 команд"),
            "execute_script", new CommandDescription("execute_script","Выполнение скрипта из файла"),
            "exit", new CommandDescription("exit","Завершение работы клиента"));
    private final ArrayDeque<String> history = new ArrayDeque<>();

    private InteractiveMode(TextReceiver textReceiver, ConsoleLoader loader, RequestHandler requestHandler, ObjectSender objectSender) {
        this.textReceiver = textReceiver;
        this.loader = loader;
        this.requestHandler = requestHandler;
        this.objectSender = objectSender;
    }

    public static InteractiveMode getInstance(TextReceiver textReceiver, ConsoleLoader loader, RequestHandler requestHandler, ObjectSender objectSender) {
        if (interactiveMode == null) {
            interactiveMode = new InteractiveMode(textReceiver, loader, requestHandler, objectSender);
            specialCommands.get("help").setCaller(new HelpCommandRealization(interactiveMode));
            specialCommands.get("history").setCaller(new HistoryCommandRealization(interactiveMode));
            specialCommands.get("exit").setCaller(new ExitCommandRealization(interactiveMode));
            specialCommands.get("execute_script").setCaller(new ExecuteScriptCommandRealization(interactiveMode));
            specialCommands.get("execute_script").setOneLineArguments(List.of(new LoadDescription<>(String.class)));
        }
        return interactiveMode;
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    private Result<HashMap<String, CommandDescription>> loadCommandDescriptionMap() {
        try {
            Result<DatagramPacket> packet = requestHandler.receivePacketWithTimeout();
            if (!packet.isSuccess()){
                return Result.failure(packet.getError().get(), "Error while receiving map of commands, error with server connection.");
            }
            Result<HashMap<String, CommandDescription>> commandDescriptionMap = deserialize(packet.getValue().get());
            if (!commandDescriptionMap.isSuccess()){
                return commandDescriptionMap;
            }
            textReceiver.println("You are authorized!");
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
            return Result.failure(e, "Error while receiving map of commands, error with server connection.");
        }
    }

    public <T> T deserialize(DatagramPacket packet) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
        } catch (IOException e){
            throw new RuntimeException("Ошибка при сериализации");
        }
        try {
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public CommandDescription getCommandDescription(String s) throws NoSuchElementException {
        if(specialCommands.containsKey(s))
            return specialCommands.get(s);
        return commandDescriptionMap.get(s);
    }

    public void start() {

        textReceiver.println("Welcome to interactive mode! Are you want to register or login? (r/l). Type \"q\" to exit.");
        while (true) {
            String command = loader.enterWithMessage(">", new LoadDescription<>(String.class)).getValue();
            if (authorizationMap.containsKey(command)) {
                Result<?> result = authorizationMap.get(command).get();
                if(result.isSuccess() && command.equals("l")){
                    break;
                }
                textReceiver.println(result.getMessage());
            } else {
                textReceiver.println("Unknown command!");
            }
        }
        textReceiver.println("Interactive mode started! Check command help to see available commands.");
        while (true) {
            CommandDescription command;
            try {
                command = loader.parseCommand(this,
                        loader.enterWithMessage(">", new LoadDescription<>(String.class)).getValue()
                );
            } catch (Exception e) {
                textReceiver.println(e.getMessage());
                continue;
            }
            try {
                command.getCaller().call();
            }catch (Exception e) {
                textReceiver.println(e.getMessage());
            }
            history.add(command.getName());
            callableManager.clear();
        }
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    private Result<Void> register() {
        enterLoginData(registerCommandDescription);
        try {
            try {
                sendCommandDescription(registerCommandDescription);
            } catch (IOException ex) {
                throw new RuntimeException("Error while sending answer, error with server connection.");
            }

            Result<DatagramPacket> packet = requestHandler.receivePacketWithTimeout();
            if (!packet.isSuccess()){
                return Result.failure(packet.getError().get(), "Error while receiving answer, error with server connection.");
            }
            Result<Void> registerResult;
            try {
                registerResult = deserialize(packet.getValue().get());
            } catch (Exception e) {
                throw new RuntimeException("Incorrect server answer.");
            }
            if(registerResult.isSuccess()) {
                textReceiver.println("You have successfully registered!");
                isAuthorized = true;
                return Result.success(null);
            }else{
                return registerResult;
            }

        } catch (Exception e) {
            return Result.failure(e, "Error while sending register command to server, error with server connection.");
        }
    }

    private void enterLoginData(CommandDescription registerCommandDescription) {
        textReceiver.print("Enter your login:");
        LoadDescription<String> loginDescription = loader.enterString(new LoadDescription<>(String.class));
        textReceiver.print("Enter your password:");
        LoadDescription<String> passwordDescription = loader.enterString(new LoadDescription<>(String.class));
        authorization = new Authorization(loginDescription.getValue(), passwordDescription.getValue());
        registerCommandDescription.setOneLineArguments(List.of(loginDescription, passwordDescription));
    }

    public void history() {
        history.stream()
                .limit(6)
                .forEach(textReceiver::println);
    }

    private Result<?> login() {
        enterLoginData(loginCommandDescription);
        try {
            sendCommandDescription(loginCommandDescription);
            Result<?> loginResult = loadCommandDescriptionMap();
            if (loginResult.isSuccess()) {
                textReceiver.println("You have successfully logged!");
                isAuthorized = true;
                return Result.success(null);
            } else {
                return loginResult;
            }
        } catch (Exception e) {
            return Result.failure(e, "Error while sending login command to server, error with server connection.");
        }
    }

    public Result<Void> exit() {
        try {
            requestHandler.closeChannel();
        } catch (IOException e) {
            textReceiver.println("Error while closing channel.");
        }
        isAuthorized = false;
        System.exit(0);
        return Result.success(null);
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void printHelp() {
        textReceiver.println("Available commands:");
        commandDescriptionMap.values()
                .forEach(commandDescription -> textReceiver.println(commandDescription.getName() + " - " + commandDescription.getDescription()));
    }

    public void sendCommandDescription(CommandDescription commandDescription) throws IOException {
        commandDescription.setAuthorization(authorization);
        objectSender.sendObject(commandDescription);
    }

    public Result<?> getResultFromServer(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Result<DatagramPacket> result = requestHandler.receivePacketWithTimeout();
        if(!result.isSuccess())
            return result;
        return deserialize(result.getValue().get());
    }

    public void addCommandToQueue(Caller caller) {
        callableManager.add(caller);
    }

    public List<Result<?>> executeAll() {
        return callableManager.callAll();
    }

    public void printToUser(Object o){
        textReceiver.println(o);
    }

    public boolean isCommandExist(String s) {
        return commandDescriptionMap.containsKey(s);
    }
}