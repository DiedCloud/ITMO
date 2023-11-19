package commandRealization.specialCommandRealization;

import common.Collection;
import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import loaders.FileLoader;
import modules.InteractiveMode;
import result.Result;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ExecuteScriptCommandRealization extends SpecialCommandRealization{
    private String path;
    private static final Deque<String> fileNameStack = new ArrayDeque<>();

    public ExecuteScriptCommandRealization(InteractiveMode interactiveMode) {
        super(interactiveMode.getCommandDescription("execute_script"), interactiveMode);
    }


    @Override
    protected void execution() {
        path = (String) commandDescription.getOneLineArguments().get(0).getValue();
        FileLoader fileLoader = new FileLoader(path);
        if (fileNameStack.contains(path)) {
            throw new RuntimeException(String.valueOf(LocalizationKeys.RECURSION_DETECTED));
        }
        fileNameStack.push(path);

        while (fileLoader.hasNext()) {
            String s;
            CommandDescription commandDescription;
            try {
                s = fileLoader.enter(new LoadDescription<>(String.class)).getValue();
                if (s == null)
                    break;
                commandDescription = fileLoader.parseCommand(interactiveMode, s);
            } catch (Exception e) {
                fileNameStack.clear();
                commandResult = Result.failure(new Exception(String.valueOf(LocalizationKeys.BAD_SCRIPT_FILE)));
                throw new RuntimeException(String.valueOf(LocalizationKeys.BAD_SCRIPT_FILE));
            }
            if (commandDescription.getName().equals("execute_script"))
                commandDescription.getCaller().call();
            else{
                interactiveMode.addCommandToQueue(commandDescription.getCaller());
            }

        }

        if (fileNameStack.size() == 1) {
            List<Result<?>> results;
            try {
                results = interactiveMode.executeAll();
            } catch (Exception e) {
                fileNameStack.clear();
                throw new RuntimeException(e);
            }
        }
        fileNameStack.pop();
    }
}
