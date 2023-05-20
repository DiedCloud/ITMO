package commands;
/**
 * Класс команды filter_starts_with_name @see Command
 */
public class FilterStartsWithName implements Command<String>{
    /**
     * Аргумент команды - подстрока.
     */
    String name;
    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param name подстрока
     */
    public FilterStartsWithName(String name){
        this.name = name;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.filter_starts_with_name(name);
    }
}
