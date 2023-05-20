package commands;
/**
 * Класс команды save
 * @see Command
 */
public class Save implements Command<String>{
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.save();
    }
}
