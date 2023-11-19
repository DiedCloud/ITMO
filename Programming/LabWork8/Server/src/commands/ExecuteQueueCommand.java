package commands;

import common.descriptions.CommandDescription;
import managers.Invoker;
import receivers.MusicReceiver;
import result.Result;

import java.util.List;

/**
 * Class ExecuteScriptCommand for executing a script from a file.
 */
public class ExecuteQueueCommand extends Command<MusicReceiver> {
    List<CommandDescription> queue;
    Invoker invoker;
    public ExecuteQueueCommand(List<CommandDescription> queue, Invoker invoker) throws Exception{
        super(MusicReceiver.GetInstance());
        this.queue = queue;
        this.invoker = invoker;
    }

    /**
     * Method execute calls the executeScriptCommand() method of the receiver object.
     * @return result for executing the command (the result of the executeScriptCommand() method of the receiver object)
     */

    @Override
    public Result<?> execute() {
        return null;
        //return receiver.executeQueue(queue, invoker);
    }
}