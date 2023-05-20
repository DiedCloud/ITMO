package commands;
/**
 * Класс команды remove_key
 * @see Command
 */
public class RemoveKey implements Command<Long> {
    /**
     * Аргумент команды - ключ.
     */
    Long key;
    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param key ключ
     */
    public RemoveKey(Long key) {
        this.key = key;
    }
    /**
     * @see Command#execute()
     */
    @Override
    public Response<Long> execute() {
        return controller.remove_key(key);
    }
}
