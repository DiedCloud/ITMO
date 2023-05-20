package commands;

import storage.City;
/**
 * Класс команды execute_script
 * @see Command
 */
public class Insert implements Command<City> {
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
    public Insert(Long key, City obj){
        this.key = key;
        this.obj = obj;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<City> execute() {
        return controller.insert(key, obj);
    }
}
