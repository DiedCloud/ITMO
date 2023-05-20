package Exceptions;

/**
 * Класс-исключение необходимый, чтобы передать сигнал о неправильном параметре, переданному методу {@link storage.CityBuilder}
 * @see storage.CityBuilder
 * @author Фролов К.Д.
 */
public class ValueException extends Exception{
    /**
     * @see ValueException
     * @see storage.CityBuilder
     * @param message сообщение с информацией почему параметры неверны и требованиями к ним
     */
    public ValueException(String message){
        super(message);
    }
}
