package commands;

/**
 * Класс контейнер, в который кладется ответ сервера и, соответственно, ответ команды. Содержит информацию об ошибках и вывод
 * @param <T> тип объекта вывода
 */
public class Response<T> {
    /**
     * Поде для информации об ошибках
     */
    String errors;
    /**
     * Поле для хранения вывода
     */
    T result;

    /**
     * Единственный конструктор, записывающий ошибки и вывод
     * @param errors информация об ошибках, если такие есть. Если нет лучше всего указать null или пустую строку
     * @param result вывод / ответ сервера
     */
    public Response(String errors, T result){
        this.errors = errors;
        this.result = result;
    }

    /**
     * Получить хранящийся в объекте результат
     * @return результат
     */
    public T getResult() {
        return result;
    }

    /**
     * Получить хранящуюся в объекте информацию об ошибках
     * @return строка с информацией об ошибках
     */
    public String getErrors(){
        return errors;
    }
}