package commands;
/**
 * Класс команды info
 * @see Command
 */
public class Info implements Command<String>{
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.info();
    }
}
