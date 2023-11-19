/**
 * Invoker - a class that manages the execution of commands.
 * Stores a list of all available commands and their names, as well as a history of executed commands and a stack of executed scripts.
 * When executeCommand is called, it invokes the corresponding command.
 */
package managers;

import common.Album;
import common.MusicBand;
import common.descriptions.CommandDescription;
import main.Main;
import receivers.*;
import result.Result;
import commands.*;

import java.util.*;
import java.util.function.Function;

public class Invoker {

    private final Map<String, Function<CommandDescription, Result<Command<MusicReceiver>>>> command_creators;

    /**
     * Constructor for the Invoker class.
     * Creates object of all available commands and registers them in the command list.
     */
    public Invoker() {
        command_creators = new HashMap<>();
        command_creators.put("add", this::add);
        command_creators.put("info", this::info);
        command_creators.put("show", this::show);
        command_creators.put("clear", this::clear);
        command_creators.put("help", this::help);
        command_creators.put("update", this::update);
        command_creators.put("remove_by_id", this::remove_by_id);
        command_creators.put("add_if_max", this::add_if_max);
        command_creators.put("remove_greater", this::remove_greater);
        command_creators.put("max_by_best_album", this::max_by_best_album);
        command_creators.put("count_by_best_album", this::count_by_best_album);
        command_creators.put("filter_by_best_album", this::filter_by_best_album);
    }

    /**
     * Executes a command with the given name.
     *
     * @param name - the name of the command.
     * @param cd   - input from client, have to contain all fields of command
     * @return an instance of the Result class containing information about the result of executing the command.
     */
    public Result<?> executeCommand(String name, CommandDescription cd) {
        Command<MusicReceiver> commandObj = null;
        if (command_creators.containsKey(name)) {
            Result<Command<MusicReceiver>> r = command_creators.get(name).apply(cd);
            if (r.isSuccess())
                commandObj = r.getValue().get();
            else
                return Result.failure(r.getError().get(), r.getMessage()); //если по неизвестной причине MusicReceiver, инициализированный в Main, при GetInstance кинет исключение
        }
        if (commandObj != null) {
            Main.logger.info("Command " + name + " executing started");
            return commandObj.execute();
        } else {
            Main.logger.error("Wrong command income");
            return Result.failure(new Exception("Команда не найдена"));
        }
    }

    public Result<Command<MusicReceiver>> add(CommandDescription cd) {
        try {
            return Result.success(new AddCommand((MusicBand) cd.getArguments().get(0).getValue()));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> info(CommandDescription cd) {
        try {
            return Result.success(new InfoCommand());
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> show(CommandDescription cd) {
        try {
            return Result.success(new ShowCommand());
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> clear(CommandDescription cd) {
        try {
            return Result.success(new ClearCommand());
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> help(CommandDescription cd) {
        try {
            return Result.success(new HelpCommand());
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> update(CommandDescription cd) {
        try {
            return Result.success(new UpdateCommand(
                    (Long) cd.getOneLineArguments().get(0).getValue(),
                    (MusicBand) cd.getArguments().get(0).getValue()
            ));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> remove_by_id(CommandDescription cd) {
        try {
            return Result.success(new RemoveByIdCommand((Long) cd.getOneLineArguments().get(0).getValue()));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> add_if_max(CommandDescription cd) {
        try {
            return Result.success(new AddIfMaxCommand((MusicBand) cd.getArguments().get(0).getValue()));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> remove_greater(CommandDescription cd) {
        try {
            return Result.success(new RemoveGreaterCommand((MusicBand) cd.getArguments().get(0).getValue()));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> max_by_best_album(CommandDescription cd) {
        try {
            return Result.success(new MaxByBestAlbumCommand());
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> count_by_best_album(CommandDescription cd) {
        try {
            return Result.success(new CountByBestAlbum((Album) cd.getArguments().get(0).getValue()));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }

    public Result<Command<MusicReceiver>> filter_by_best_album(CommandDescription cd) {
        try {
            return Result.success(new FilterByBestAlbum((Album) cd.getArguments().get(0).getValue()));
        } catch (Exception e) {
            return Result.failure(e, e.getMessage());
        }
    }
}