package commands;

import storage.City;
/**
 * Класс команды replace_if_greater
 * @see Command
 */
public class ReplaceIfGreater implements Command<String>{
    /**
     * Аргумент команды - ключ
     */
    Long key;
    /**
     * Аргумент команды - объект {@link City}
     */
    City obj;
    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param key ключ
     * @param obj объект {@link City}
     */
    public ReplaceIfGreater(Long key, City obj){
        this.key = key;
        this.obj = obj;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.replace_if_greater(key, obj);
    }
}
