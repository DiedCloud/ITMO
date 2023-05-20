package commands;

import storage.City;

/**
 * Класс команды remove_greater
 *
 * @see Command
 */
public class RemoveGreater implements Command<String> {
    /**
     * Аргумент команды - объект {@link City}
     */
    City element;
    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param element объект {@link City}
     */
    public RemoveGreater(City element) {
        this.element = element;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.remove_greater(element);
    }
}
