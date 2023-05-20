package storage;

/**
 * Класс описывающий плоские координаты чего-либо
 * @author Фролов К.Д.
 */
public class Coordinates {
    /**
     * Координата X
     */
    private Integer  x;
    /**
     * Координата Y: значение поля должно быть больше -805
     */
    private Integer  y;

    /**
     *
     * @param x координата X
     * @param y координата Y
     */
    public Coordinates(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    /**
     * Для отображения координат пользователю.
     * @return Объект, преобразованный в строку.
     */
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    /**
     * @return {@link Coordinates#x}
     */
    public Integer getX(){
        return x;
    }

    /**
     * @return {@link Coordinates#y}
     */
    public Integer getY() {
        return y;
    }
}