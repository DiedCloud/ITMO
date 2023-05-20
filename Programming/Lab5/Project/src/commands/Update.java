package commands;

import storage.City;
/**
 * Класс команды update
 * @see Command
 */
public class Update implements Command<String>{
    /**
     * Аргумент команды - id
     */
    long id;
    /**
     * Аргумент команды - объект {@link City}
     */
    City obj;
    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param id id
     * @param obj объект {@link City}
     */
    public Update(long id, City obj){
        this.id = id;
        this.obj = obj;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return controller.update(id, obj);
    }
}
