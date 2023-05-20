package commands;

/**
 * Класс команды execute_script
 * @see Command
 */
public class ExecuteScript implements Command<String>{
    /**
     * Аргумент команды - путь к файлу.
     */
    public String filename;

    /**
     * Единственный конструктор, задает все аргументы команды.
     * @param filename путь к файлу
     */
    public ExecuteScript(String filename){
        this.filename = filename;
    }

    /**
     * @see Command#execute()
     */
    @Override
    public Response<String> execute() {
        return new Response<>("", "Скрипт исполнен.");
    }
}
