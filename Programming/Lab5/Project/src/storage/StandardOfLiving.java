package storage;

/**
 * Перечисление доступных типов уровня жизни. Используется для описания объекта {@link City}
 * @author Фролов К.Д.
 */
public enum StandardOfLiving {
    /**
     * Наивысший
     */
    ULTRA_HIGH,
    /**
     * Очень высокий
     */
    VERY_HIGH,
    /**
     * Высокий
     */
    HIGH,
    /**
     * Кошмар. (тут точно не повезло...)
     */
    NIGHTMARE;
}