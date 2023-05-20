package commands;

import java.util.Stack;
/**
 * Класс команды execute_script. Хранит в себе объект {@link Stack<String>} введенных пользователем команд.
 * (В текущей реализации это просто строчный ввод пользователя. Можно сделать и хранение объектов команд.)
 * @see Command
 */
public class History implements Command<String>{
    /**
     * Поле для хранения самой истории.
     */
    Stack<String> history = new Stack<>();

    /**
     * Добавляет в историю команду.
     * @param command Введенная пользователем строка, содержавшая команду.
     */
    public void Push(String command){
        history.push(command);
    }

    /**
     * Возвращает последнюю команду из истории, предварительно удаляя ее с вершины стэка.
     * @return Когда-то введенная пользователем строка, содержавшая команду.
     */
    public String Pop(){
        return history.pop();
    }
    /**
     * Возвращает строку, из всех содержащихся в стэке команд, разделенных символом переноса строки.
     * @see Command#execute()
     */
    public Response<String> execute() {
        String res = "";
        for (String com: history){
            res += com + "\n";
        }
        return new Response<String>("", res);
    }
}
