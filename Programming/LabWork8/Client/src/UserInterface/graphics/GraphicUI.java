package UserInterface.graphics;

import TextReceivers.LocalizedTextReceiver;
import loaders.ConsoleLoader;
import modules.InteractiveMode;
import modules.Notifier;
import result.Result;

import java.util.Map;
import java.util.function.Supplier;

public class GraphicUI {
    private static GraphicUI graphicUI;
    private static Map<String, String> localizationMap;
    private InteractiveMode interactiveMode;

    private LocalizedTextReceiver textReceiver = new LocalizedTextReceiver("en_AU", "en_AU");

    private ConsoleLoader loader = new ConsoleLoader(textReceiver);

    private static Map<String, Supplier<Result<?>>> authorizationMap;
    public GraphicUI(InteractiveMode interactiveMode) {
        this.interactiveMode = interactiveMode;
    }

    public static GraphicUI getInstance(InteractiveMode interactiveMode){
        if(graphicUI == null)
            graphicUI = new GraphicUI(interactiveMode);
        authorizationMap = Map.of("l", interactiveMode::login,
                "r", interactiveMode::register,
                "q", interactiveMode::exit
        );
        localizationMap = Map.of("Australian-English", "en_AU",
                "Russian", "ru",
                "Danish","da",
                "Icelandic","is");

        return graphicUI;
    }

    public void start(Notifier n){
        StartForm form = new StartForm(localizationMap, interactiveMode, n);
        form.pack();
    }
}
