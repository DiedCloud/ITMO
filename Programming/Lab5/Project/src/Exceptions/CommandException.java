package Exceptions;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс-исключение необходимый, чтобы передать сигнал об отсутствии введенной пользователем команды
 *  Вызывается в {@link client.Invoker#parse(String, Scanner)}, обрабатывается в {@link client.Invoker#run(InputStream)}
 * @author Фролов К.Д.
 */
public class CommandException extends Exception {
    /**
     * @param message сообщение об отсутствии такой команды в наборе команд
     */
    public CommandException(String message) {
        super(message);
    }
}