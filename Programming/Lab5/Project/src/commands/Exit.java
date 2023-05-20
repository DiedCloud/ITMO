package commands;

import Exceptions.ExitException;
/**
 * Класс команды exit
 * @see Command
 */
public class Exit implements Command<String>{
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() throws ExitException{
        throw new ExitException("До свидания!");
    }
}
