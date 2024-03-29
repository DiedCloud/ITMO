package commands;
/**
 * Класс команды help
 * @see Command
 */
public class Help implements Command<String>{
    /**
     * В текущей реализации сразу возвращает заранее написанный текст-справку.
     * @see Command#execute()
     */
    public Response<String> execute(){
        String message = "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "insert key {element} : добавить новый элемент с заданным ключом\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_key key : удалить элемент из коллекции по его ключу\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "history : вывести последние 14 команд (без их аргументов)\n" +
                "replace_if_greater key {element} : заменить значение по ключу, если новое значение больше старого\n" +
                "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку\n" +
                "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                "print_unique_meters_above_sea_level : вывести уникальные значения поля metersAboveSeaLevel всех элементов в коллекции";
        return new Response<>(null, message);
    }
}
