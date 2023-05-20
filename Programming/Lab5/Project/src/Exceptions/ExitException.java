package Exceptions;

import commands.Exit;

import java.io.InputStream;

/**
 * Класс-исключение необходимый, чтобы передать сигнал о необходимости закончить выполнение программы обратно по stacktrace и понять что это именно запрос на прекращение исполнения.
 *  Вызывается в {@link Exit#execute()}, обрабатывается в {@link client.Invoker#run(InputStream)}
 * @author Фролов К.Д.
 */
public class ExitException extends Exception{
    /**
     * @see ExitException
     * @param message сообщение с прощанием, так как используется для выхода из программы
     */
    public ExitException(String message) {
        super(message);
    }
}
