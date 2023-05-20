package commands;

/**
 * Класс команды clear
 * @see Command
 */
public class Clear implements Command<String>{
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.clear();
    }
}
