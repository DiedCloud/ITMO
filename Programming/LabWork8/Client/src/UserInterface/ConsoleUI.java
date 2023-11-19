package UserInterface;

import TextReceivers.LocalizedTextReceiver;
import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import loaders.ConsoleLoader;
import managers.BaseTextReceiver;
import modules.InteractiveMode;
import TextReceivers.TextReceiver;
import result.Result;

import java.util.Map;
import java.util.function.Supplier;

public class ConsoleUI {

    private static ConsoleUI consoleUI;
    private static Map<String, String> localizationMap;
    private InteractiveMode interactiveMode;

    private LocalizedTextReceiver textReceiver = new LocalizedTextReceiver("en_AU", "en_AU");

    private ConsoleLoader loader = new ConsoleLoader(textReceiver);

    private static Map<String, Supplier<Result<?>>> authorizationMap;
    public ConsoleUI(InteractiveMode interactiveMode) {
        this.interactiveMode = interactiveMode;
    }

    public static ConsoleUI getInstance(InteractiveMode interactiveMode){
        if(consoleUI == null)
            consoleUI = new ConsoleUI(interactiveMode);
        authorizationMap = Map.of("l", interactiveMode::login,
                "r", interactiveMode::register,
                "q", interactiveMode::exit
        );
        localizationMap = Map.of("Australian-English", "en_AU",
                "Russian", "ru",
                "Danish","da",
                "Icelandic","is");

        return consoleUI;
    }

    public void start(){
        textReceiver.print("Choose localization: ");
        for (String key : localizationMap.keySet())
            textReceiver.print(key + " ");
        String localization = loader.enterWithMessage(">", new LoadDescription<>(String.class)).getValue();
        while (!localizationMap.containsKey(localization)) {
            textReceiver.println(LocalizationKeys.UNKNOWN_LANGUAGE);
            localization = loader.enterWithMessage(">", new LoadDescription<>(String.class)).getValue();
        }
        textReceiver.switchLocalization(localizationMap.get(localization));
        interactiveMode.setTextReceiver(textReceiver);

        textReceiver.println(LocalizationKeys.WELCOME);
        while (true) {
            String command = loader.enterWithMessage(">", new LoadDescription<>(String.class)).getValue();
            if (authorizationMap.containsKey(command)) {
                Result<?> result = authorizationMap.get(command).get();
                if(result.isSuccess() && command.equals("l")){
                    break;
                }
                textReceiver.println(result.getMessage());
            } else {
                textReceiver.println(LocalizationKeys.UNKNOWN_COMMAND);
            }
        }

        textReceiver.println(LocalizationKeys.INTERACTIVE_START);
        while (true) {
            CommandDescription command;
            try {
                command = loader.parseCommand(interactiveMode,
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
            interactiveMode.addCommandToHistory(command.getName());
            interactiveMode.clearCommandQueue();
        }
    }
}
