import client.Invoker;
import storage.Receiver;

import java.io.*;

/**
 * Главный класс. Здесь проверяется доступ к csv файлу, инициализируется контроллер коллекции и создается Invoker.
 * @author Фролов К.Д.
 */
public class Main {
    /**
     * Единственный метод {@link Main}. Выполняется первым и начинает всю программу.
     * @param args Аргументы переданные при запуске программы... не используются.
     */
    public static void main(String[] args) {
        if (System.getenv("FILE_NAME") == null) {
            System.out.println("Передайте в переменную окружения FILE_NAME путь к файлу");
            System.exit(0);
        }
        try {
            if(new File(System.getenv("FILE_NAME")).exists()){
            if (!new File(System.getenv("FILE_NAME")).canRead()) {
                System.out.println("Недостаточно прав для чтения файла");
                System.exit(0);
            }}
            else{
                System.out.println("Несуществующий файл");
                System.exit(0);
            }
        } catch (NullPointerException e) {
            System.out.println("Передайте в переменную окружения FILE_NAME путь к файлу");
            System.exit(0);
        }
        Receiver.GetInstance();
        Invoker invoker = new Invoker();
        invoker.run(System.in);
    }
}