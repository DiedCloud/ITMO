package commands;
/**
 * Класс команды show
 * @see Command
 */
public class Show implements Command<String> {
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.show();
    }
}
