package storage;

/**
 * Класс описывающий человека
 * @author Фролов К.Д.
 */
public class Human {
    /**
     * Рост: значение поля должно быть больше 0
     */
    private final long height;

    /**
     * Привет, я твой единственный конструктор, ведь все что у тебя есть - это твой рост
     * @param height рост {@link Human#height}
     */
    public Human(long height){
        this.height = height;
    }

    /**
     * Для отображения информации о данном человеке пользователю.
     * @return Объект, преобразованный в строку.
     */
    @Override
    public String toString() {
        return "Human{" +
                "height=" + height +
                '}';
    }

    /**
     * @return {@link Human#height}
     */
    public long getHeight(){
        return height;
    }
}