package storage;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс описывающий объекты городов, хранимые в коллекции.
 * @author Фролов К.Д.
 */
public class City implements Comparable<City> {
    /**
     * id города:
     * поле не может быть null, значение поля должно быть больше 0, значение этого поля должно быть уникальным, значение этого поля должно генерироваться автоматически
     */
    private final long id;
    /**
     * Название города:
     *  поле не может быть null, строка не может быть пустой
     */
    private final String name;
    /**
     * Координаты города:
     * поле не может быть null
     */
    private final Coordinates coordinates;
    /**
     * Дата создания записи о городе:
     * поле не может быть null, значение этого поля должно генерироваться автоматически
     */
    private final java.time.LocalDateTime creationDate;
    /**
     * Площадь города:
     * значение поля должно быть больше 0, поле не может быть null
     */
    private final float area;
    /**
     * Население города:
     * значение поля должно быть больше 0
     */
    private final Long population;
    /**
     * Значение высоты над уровнем моря
     */
    private final int metersAboveSeaLevel;
    /**
     * Климат:
     * поле не может быть null
     */
    private final Climate climate;
    /**
     * Власть:
     * поле не может быть null
     */
    private final Government government;

    /**
     * Уровень жизни:
     * поле не может быть null
     */
    private final StandardOfLiving standardOfLiving;

    /**
     * Мэр:
     * поле не может быть null
     */
    private final Human governor;


    /**
     * Конструктор с автоматической генерацией {@link City#id} и {@link City#creationDate}.
     * Может использоваться для вводимых пользователем городов.
     * @param name название города
     * @param coordinates координаты города
     * @param area площадь города
     * @param population население города
     * @param metersAboveSeaLevel значение высоты над уровнем морем
     * @param climate климат
     * @param government власть
     * @param standardOfLiving уровень жизни
     * @param governor мэр
     * @see City#City(long, String, Coordinates, LocalDateTime, float, Long, int, Climate, Government, StandardOfLiving, Human)
     */
    public City(String name, Coordinates coordinates,
                float area, Long population, int metersAboveSeaLevel,
                Climate climate, Government government,
                StandardOfLiving standardOfLiving, Human governor) {
        id = System.currentTimeMillis();
        creationDate = java.time.LocalDateTime.now();
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.climate = climate;
        this.government = government;
        this.standardOfLiving = standardOfLiving;
        this.governor = governor;
    }

    /**
     * Конструктор без автоматической генерации {@link City#id} и {@link City#creationDate}.
     * @param id id города
     * @param name название города
     * @param coordinates координаты города
     * @param creationDate дата создания записи о городе
     * @param area площадь города
     * @param population население города
     * @param metersAboveSeaLevel значение высоты над уровнем морем
     * @param climate климат
     * @param government власть
     * @param standardOfLiving уровень жизни
     * @param governor мэр
     * @see City#City(String, Coordinates, float, Long, int, Climate, Government, StandardOfLiving, Human)
     */
    protected City(long id, String name, Coordinates coordinates, java.time.LocalDateTime creationDate,
                float area, Long population, int metersAboveSeaLevel,
                Climate climate, Government government,
                StandardOfLiving standardOfLiving, Human governor) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.climate = climate;
        this.government = government;
        this.standardOfLiving = standardOfLiving;
        this.governor = governor;
    }

    /**
     * Для отображения информации о данном городе пользователю.
     * @return Объект, преобразованный в строку.
     */
    @Override
    public String toString() {
        return "id=" + id +
                "\t name='" + name + '\'' +
                "\t coordinates=" + coordinates +
                "\t creationDate=" + creationDate +
                "\t area=" + area +
                "\t population=" + population +
                "\t metersAboveSeaLevel=" + metersAboveSeaLevel +
                "\t climate=" + climate +
                "\t government=" + government +
                "\t standardOfLiving=" + standardOfLiving +
                "\t governor=" + governor;
    }

    /**
     * Для сохранения объектов коллекции в csv файл.
     * @return Объект, преобразованный в строку из всех параметров через запятую.
     */
    public String toCommaRow(){
        return id + "," + name + "," + coordinates.getX() + "," + coordinates.getY() + "," + creationDate +
                "," + area + "," + population + "," + metersAboveSeaLevel +
                "," + climate + "," + government +  "," + standardOfLiving +
                "," + governor.getHeight();
    }

    /**
     * @param o город для сравнения
     * @return true если города совпадают по всем параметрам, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(population, city.population) &&
                Objects.equals(id, city.id) &&
                Objects.equals(name, city.name) &&
                Objects.equals(coordinates, city.coordinates) &&
                Objects.equals(creationDate, city.creationDate) &&
                Objects.equals(area, city.area) &&
                Objects.equals(metersAboveSeaLevel, city.metersAboveSeaLevel) &&
                climate == city.climate &&
                government == city.government &&
                standardOfLiving == city.standardOfLiving &&
                Objects.equals(governor, city.governor);
    }

    /**
     * @return Objects.hash(id, name, coordinates, creationDate, area, population, metersAboveSeaLevel, climate, government, standardOfLiving, governor);
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate,
                area, population, metersAboveSeaLevel, climate,
                government, standardOfLiving, governor);
    }

    /**
     * Метод сравнения городов (по населению) для сортировки
     * @param o город для сравнения
     * @return -1 если текущий меньше, 1 если текущий больше, 0 если равны
     */
    @Override
    public int compareTo(City o) {
        if (this.population < o.population) {
            return -1;/* текущее меньше полученного */
        } else if (this.population > o.population) {
            return 1;/* текущее больше полученного */
        }
        return 0;/* текущее равно полученному */
    }

    /**
     * @return {@link City#id}
     */
    public long getId() {
        return id;
    }

    /**
     * @return {@link City#name}
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@link City#metersAboveSeaLevel}
     */
    public int getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }
}
