package commands;
/**
 * Класс команды filter_contains_name
 * @see Command
 */
public class FilterContainsName implements Command<String>{
    /**
     * Аргумент команды - подстрока.
     */
    String name;

    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param name подстрока
     */
    public FilterContainsName(String name){
        this.name = name;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.filter_contains_name(name);
    }
}
