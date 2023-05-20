package commands;
/**
 * Класс команды print_unique_meters_above_sea_level
 * @see Command
 */
public class PrintUniqueMetersAboveSeaLevel implements Command<String>{
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.print_unique_meters_above_sea_level();
    }
}
