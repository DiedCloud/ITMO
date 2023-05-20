package client;

import Exceptions.CommandException;
import Exceptions.ExitException;
import Exceptions.ValueException;
import commands.*;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Класс из которого происходит все управление программой.
 *
 * @author Фролов К.Д.
 */
public class Invoker {
    /**
     * Объект {@link History} для хранения выполненных команд.
     */
    History history;
    /**
     * Очередь команд на исполнение. Заполняется в {@link Invoker#checkScript(InputStream)}, очищается, предварительно исполнившись в {@link Invoker#parse(String, Scanner)}
     */
    ArrayList<Command<?>> queue; //for execute_script

    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся.
     *
     * @param c           массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return {@link Invoker#history}, если количество аргументов и сами аргументы верные
     */
    private History History(String[] c, Scanner reader) {
        return history;
    }

    /**
     * Словарь с ключами в виде названий команд и значениями в виде ссылок на методы, преобразующие массив с командой и аргументами в объект {@link Command}
     */
    HashMap<String, BiFunction<String[], Scanner, Command<?>>> commands;
    /**
     * Стэк открытых файлов. Нужен, чтобы избежать бесконечной рекурсии исполнения скриптов. Используется в {@link Invoker#ExecuteScript(String[], Scanner)}
     */
    Stack<String> files;

    /**
     * Конструктор. Здесь инициализируются {@link Invoker#queue}, {@link Invoker#history}, {@link Invoker#files}. Инициализируется и заполняется {@link Invoker#commands}.
     */
    public Invoker() {
        files = new Stack<>();
        history = new History();
        queue = null;
        commands = new HashMap<>();
        commands.put("help", Parser::Help);
        commands.put("info", Parser::Info);
        commands.put("show", Parser::Show);
        commands.put("insert", Parser::Insert);
        commands.put("update", Parser::Update);
        commands.put("remove_key", Parser::RemoveKey);
        commands.put("clear", Parser::Clear);
        commands.put("save", Parser::Save);
        commands.put("execute_script", this::ExecuteScript);
        commands.put("exit", Parser::Exit); //есть хардкод обработка в checkScript!!!
        commands.put("remove_greater", Parser::RemoveGreater);
        commands.put("history", this::History);
        commands.put("replace_if_greater", Parser::ReplaceIfGreater);
        commands.put("filter_contains_name", Parser::FilterContainsName);
        commands.put("filter_starts_with_name", Parser::FilterStartsWithName);
        commands.put("print_unique_meters_above_sea_level", Parser::PrintUniqueMetersAboveSeaLevel);
    }

    /**
     * Единственный публичный метод {@link Invoker}. Отсюда происходит запуск интерактивного режима управления коллекцией.
     * Просит ввести команду, а дальше передает управление {@link Invoker#parse(String, Scanner)}. Если это действительно верная команда, то запишет ее в {@link Invoker#history}.
     *
     * @param inputStream Входной поток данных, из которого ожидается поступление команд. Обычно это {@link System#in}.
     */
    public void run(InputStream inputStream) {
        Scanner reader = new Scanner(new InputStreamReader(inputStream));// System.in
        String in = null;
        while (true) {
            try {
                System.out.print(">>> ");
                try {
                    in = reader.nextLine();
                } catch (Exception e) {
                    System.out.println("Ошибка ввода");
                    System.out.println("Чтобы узнать список доступных команд введите help");
                    reader = new Scanner(new InputStreamReader(inputStream));
                    continue;
                }
                Command<?> commandObj = parse(in, reader);
                if (commandObj != null && commandObj.getClass() != History.class) { // если в историю только консоль, то добавить inputStream.equals(System.in)
                    history.Push(in);
                }
            } catch (CommandException e) {
                System.out.println(e.getMessage());
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                break;
            } catch (NoSuchElementException e) {
                System.out.println("В файле " + files.peek() + " строк больше нет. Выполнение закончено.");
                break;
            }
        }
    }

    /**
     * Получает введенную строку, разбивает ее по пробелу, ищет название команды в {@link Invoker#commands}.
     * Если находит, то передает управление соответствующему методу, а потом применяет метод {@link Command#execute()} к полученной команде
     * Если это {@link Invoker#ExecuteScript(String[], Scanner)}, то выполняет полученную очередь команд, а не одну команду
     *
     * @param s           строка введенная пользователем
     * @param reader Объект, прочитавший строку
     * @return Объект исполненной команды, чтобы {@link Invoker#run(InputStream)} записал ее в {@link Invoker#history}.
     * @throws CommandException если такой команды не существует (нет в ключах словаря)
     * @throws ExitException    если была команда {@link Exit}
     */
    private Command<?> parse(String s, Scanner reader) throws CommandException, ExitException {
        String[] c = s.trim().split(" ");
        String commandName = c[0];

        Command<?> commandObj = null;
        if (commands.containsKey(commandName)) {
            commandObj = commands.get(commandName).apply(c, reader);
        } else {
            throw new CommandException("Нет такой команды. Вы можете ввести команду Help чтобы увидеть список команд.");
        }

        if (commandObj != null) {

            if (queue != null) {
                for (Command<?> i : queue) {
                    Response<?> res = i.execute();
                    if (res.getErrors() == null || res.getErrors().equals("")) {
                        System.out.println(res.getResult());
                    } else {
                        System.out.println(res.getErrors());
                    }
                }
                queue = null;
            }//очищаем очередь исполнения, если есть

            Response<?> response = commandObj.execute();
            if (response.getErrors() == null || response.getErrors().equals("")) {
                System.out.println(response.getResult());
                return commandObj;
            } else {
                System.out.println(response.getErrors());
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link ExecuteScript}, проверяя файл.
     * Передает управление {@link Invoker#checkScript(InputStream)}, если файл хороший и нужно проверить содержание и собрать очередь команд из содержимого
     *
     * @param c           массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link ExecuteScript}, если количество аргументов, сами аргументы и файл верные
     */
    private ExecuteScript ExecuteScript(String[] c, Scanner reader) {
        ExecuteScript commandObj = null;
        if (c.length == 2) {
            String fileName = c[1];
            if (!files.contains(fileName)) {
                try {
                    files.push(fileName);
                    if (!checkScript(new FileInputStream(fileName))) {
                        files.pop();
                        return null;
                    }
                    files.pop();
                } catch (FileNotFoundException e) {
                    files.pop();
                    System.out.println("Файл не найден, проверьте, пожалуйста, путь к файлу.");
                    return null;
                }
                catch (SecurityException e){
                    files.pop();
                    System.out.println("Файл не найден, проверьте, пожалуйста, права доступа к файлу.");
                    return null;
                }
                catch (Exception e) {
                    files.pop();
                    System.out.println(e.getMessage());
                    return null;
                }
            } else {
                System.out.println("Файл " + fileName + " уже исполняется. Нельзя запустить его повторно пока он не закончит свое выполнение.");
                return null;
            }
            commandObj = new ExecuteScript(fileName);
        } else {
            System.out.println("У команды execute_script 1 аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }

    /**
     * Проверяет содержимое файла: пытается применить методы из {@link Invoker#commands} к строкам из файла.
     * Если хоть один метод вернет null команду, то возвращается false, так как файл с ошибкой и исполнять его нельзя.
     *
     * @param inputStream Объект текущего потока, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return true, если все команды внутри скрипта проходят валидацию (включая подскрипты, проверка рекурсивная) и можно исполнять команды из очереди
     */
    private Boolean checkScript(InputStream inputStream) {
        queue = new ArrayList<>();
        Scanner reader = new Scanner(new InputStreamReader(inputStream));// fileStream
        String in;
        while (true) {
            try {
                in = reader.nextLine();
                String[] c = in.trim().split(" ");
                String commandName = c[0];
                if (c[0].equals("exit")) { // если в файле встречается команда exit, то просто выходим из файла, не из проги
                    return true;
                }
                if (commands.containsKey(commandName)) {
                    Command<?> commandOBJ = commands.get(commandName).apply(c, reader);
                    if (commandOBJ != null) {
                        queue.add(commandOBJ);
                    }
                    else{
                        queue = null;
                        System.out.println("Скрипт содержит ошибку и не будет исполнен.");
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (NoSuchElementException e) {
                return true;
            }
        }
    }
}