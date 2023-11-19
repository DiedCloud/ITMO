package loaders;

import commandRealization.CommandRealization;
import common.LocalizationKeys;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import managers.AbstractLoader;
import managers.BaseTextReceiver;
import modules.InteractiveMode;
import parsers.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ConsoleLoader extends AbstractLoader {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public ConsoleLoader(BaseTextReceiver textReceiver) {
        super(textReceiver);
        parser = new Parser();
    }

    @Override
    public <T extends LoadDescription<?>> T enterDate(T t) {
        String s = null;
        while (true) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                return (T)t.setValue(parser.parse(s, t.getType()));
            } catch (Exception e) {
                textReceiver.println(e.getMessage());
            }
        }
    }

    public CommandDescription parseCommand(InteractiveMode interactiveMode, String command) {
        // разбиение по пробелам или отдельных слов в кавычках с помощью регулярного выражения
        List<String> commandParts = splitStringWithQuotes(command);

        if (commandParts.size() == 0) {
            throw new RuntimeException("ERROR_EMPTY_COMMAND");
        }
        if (interactiveMode.isCommandExist(commandParts.get(0))) {
            CommandDescription commandDescription;
            try {
                commandDescription = interactiveMode.getCommandDescription(commandParts.get(0)).clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (commandDescription.getOneLineArguments() == null) {
                if (commandParts.size() != 1) {
                    throw new RuntimeException("ERROR_WRONG_NUMBER_OF_ARGUMENTS");
                }
            } else {
                if (commandDescription.getOneLineArguments().size() != commandParts.size() - 1) {
                    throw new RuntimeException("ERROR_WRONG_NUMBER_OF_ARGUMENTS");
                }
            }
            if (commandDescription.getOneLineArguments() != null) {
                IntStream.range(0, commandDescription.getOneLineArguments().size())
                        .forEach(i -> commandDescription.getOneLineArguments()
                                .get(i)
                                .setValue(
                                        parse(commandParts.get(i + 1),
                                                commandDescription.getOneLineArguments()
                                                        .get(i)
                                                        .getType()
                                        )
                                ));
            }
            if (commandDescription.getArguments() == null) {
                return commandDescription;
            }
            commandDescription.setLoader(this);
            if(commandDescription.getCaller() instanceof CommandRealization)
                ((CommandRealization)(commandDescription.getCaller()))
                        .setCommandDescription(commandDescription);
            return commandDescription;
        } else {
            throw new RuntimeException("UNKNOWN_COMMAND");
        }
    }

    @Override
    public <T extends LoadDescription<?>> T enterWrapper(T t) {
        String s = null;
        while (true) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                return (T) t.setValue(parse(s, t.getType()));
            } catch (Exception e) {
                textReceiver.println(e.getMessage());
            }
        }
    }

    @Override
    public LoadDescription<String> enterString(LoadDescription<String> description) {
        String s;
        while (true) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                return description.setValue(s);
            } catch (Exception e) {
                textReceiver.println(e.getMessage());
            }
        }
    }

    @Override
    public <T extends LoadDescription<Enum>> T enterEnum(T t) {
        String s;
        while (true) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                return (T) t.setValue(parse(s, (Class<Enum>) t.getType()));
            } catch (Exception e) {
                textReceiver.println(e.getMessage());
            }
        }
    }

    public List<String> splitStringWithQuotes(String input) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        int start = 0;
        for (int current = 0; current < input.length(); current++) {
            if (input.charAt(current) == '\"') {
                inQuotes = !inQuotes;
            }
            if (current == input.length() - 1) {
                result.add(input.substring(start).replace("\"",""));
            } else if (input.charAt(current) == ' ' && !inQuotes) {
                result.add(input.substring(start, current).replace("\"",""));
                start = current + 1;
            }
        }
        return result;
    }

}