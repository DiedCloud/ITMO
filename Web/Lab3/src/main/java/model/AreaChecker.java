package model;

/**
 * Класс, цель которого определить попадание точки в область.
 * Содержит единственный статический метод {@link AreaChecker#getResult(double, double, double)}, реализующий эту задачу.
 */
public class AreaChecker {
    /**
     * По координатам точки и радиусу области определяет попадание. Форма области предопределена свыше (вариантом)
     * @param x абсцисса точки
     * @param y ордината точки
     * @param r радиус области
     * @return True если попадание
     */
    public static boolean getResult(double x, double y, double r) {
        // check 1st square -- 1/4 triangle
        if (x >= 0 && y >= 0) {
            if (x + 2*y <= r)
                return true;
        }
        // check 2nd square -- square
        if (x <= 0 && y >= 0) {
            if (-x <= r && y <= r/2)
                return true;
        }
        // check 3rd square -- circle
        if (x <= 0 && y <= 0) {
            return Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(r, 2) <= 0.000_001;
        }
        // check 4th square -- always false
        // also if all checks above fails...
        return false;
    }
}
