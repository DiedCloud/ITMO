package commands;

import Exceptions.ExitException;
import storage.Receiver;

/**
 * Базовый интерфейс для всех возможных команд.
 * @param <T> Тип для параметризованного класса {@link Response}. Этому типу соответствует значение в ответе команды.
 */
public interface Command<T> {
    /**
     * Ссылка на (единственный) объект {@link Receiver}
     */
    Receiver controller = Receiver.GetInstance();

    /**
     * Метод, вызывающий исполнение команды, передавая вызов соответствующему методу.
     * @return Результат исполнения команды или сообщение об успехе. В случае ошибки возвращает сообщение об ошибке.
     * @throws ExitException Если команда должна породить выход из приложения, то пробрасывается это исключение.
     */
    Response<T> execute() throws ExitException;
}
