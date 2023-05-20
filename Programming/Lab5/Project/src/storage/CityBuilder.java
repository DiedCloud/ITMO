package storage;

import Exceptions.ValueException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Класс поэтапно строящий объекты {@link City}. Содержит методы, проверяющие корректность поля, задающие его значение и
 * возвращающие объект {@link CityBuilder}, чтобы можно было использовать их по цепочке, и метод возвращающий объект {@link City} с заданными этими методами полями.
 * @author Фролов К.Д.
 */
public class CityBuilder {
    /**
     * id города:
     * поле не может быть null, значение поля должно быть больше 0, значение этого поля должно быть уникальным, значение этого поля должно генерироваться автоматически
     */
    private long id;
    /**
     * Название города:
     * поле не может быть null, строка не может быть пустой
     */
    private String name;
    /**
     * Координаты города:
     * поле не может быть null
     */
    private Coordinates coordinates;
    /**
     * Дата создания записи о городе:
     * поле не может быть null, значение этого поля должно генерироваться автоматически. Координата Y > -805
     */
    private java.time.LocalDateTime creationDate;
    /**
     * Площадь города:
     * значение поля должно быть больше 0, поле не может быть null
     */
    private float area;
    /**
     * Население города:
     * значение поля должно быть больше 0
     */
    private Long population;
    /**
     * Значение высоты над уровнем моря
     */
    private int metersAboveSeaLevel;
    /**
     * Климат:
     * поле не может быть null
     */
    private Climate climate;
    /**
     * Власть:
     * поле не может быть null
     */
    private Government government;
    /**
     * Уровень жизни:
     * поле не может быть null
     */
    private StandardOfLiving standardOfLiving;
    /**
     * Мэр:
     * поле не может быть null
     */
    private Human governor;

    /**
     * @param id id города
     * @return {@link CityBuilder} с заданным {@link CityBuilder#id}
     * @throws ValueException если {@link CityBuilder#id} принимает недопустимое значение (см. описание {@link CityBuilder#id})
     */
    public CityBuilder setID(long id) throws ValueException {
        if (id > 0) {
            this.id = id;
        } else {
            throw new ValueException("ID должен быть больше 0");
        }
        return this;
    }

    /**
     * @param name название города
     * @return {@link CityBuilder} с заданным {@link CityBuilder#name}
     * @throws ValueException если {@link CityBuilder#name} принимает недопустимое значение (см. описание {@link CityBuilder#name})
     */
    public CityBuilder setName(String name) throws ValueException {
        if (name.equals("")) {
            throw new ValueException("Поле name не может быть пустым");
        }
        this.name = name;
        return this;
    }

    /**
     * @param X координата X
     * @param Y координата Y
     * @return {@link CityBuilder} с заданными {@link CityBuilder#coordinates}
     * @throws ValueException если {@link CityBuilder#coordinates} принимают недопустимое значение (см. описание {@link CityBuilder#coordinates})
     */
    public CityBuilder setCoordinates(String X, String Y) throws ValueException {
        try {
            int Xpos = Integer.parseInt(X);
            int Ypos = Integer.parseInt(Y);
            if (Ypos < -804) {
                throw new ValueException("Координата Y не может быть меньше -804");
            }
            this.coordinates = new Coordinates(Xpos, Ypos);
        } catch (NumberFormatException e) {
            throw new ValueException("Координата должна быть целым числом в диапазоне от -2147483647 до 2147483647");
        }
        return this;
    }

    /**
     * @param creationDate дата создания записи о городе
     * @return {@link CityBuilder} с заданным {@link CityBuilder#creationDate}
     * @throws ValueException если {@link CityBuilder#creationDate} принимает недопустимое значение (см. описание {@link CityBuilder#creationDate})
     * @throws DateTimeParseException если переданная строка не соответствует формату даты
     */
    public CityBuilder setCreationDate(String creationDate) throws ValueException, DateTimeParseException {
        if (!creationDate.equals("")) {
            this.creationDate = LocalDateTime.parse(creationDate);
        } else {
            throw new ValueException("Поле дата создания должно иметь значение");
        }
        return this;
    }

    /**
     * @param area площадь города
     * @return {@link CityBuilder} с заданным {@link CityBuilder#area}
     * @throws ValueException если {@link CityBuilder#area} принимает недопустимое значение (см. описание {@link CityBuilder#area})
     */
    public CityBuilder setArea(String area) throws ValueException {
        try {
            float a = Float.parseFloat(area);
            if (a <= 0) {
                throw new ValueException("Площадь должна быть больше 0");
            }
            this.area = a;
        } catch (NumberFormatException e) {
            throw new ValueException("Площадь должна быть числом в диапазоне от -3.4*10^38 до 3.4*10^38");
        }
        return this;
    }

    /**
     * @param population население города
     * @return {@link CityBuilder} с заданным {@link CityBuilder#population}
     * @throws ValueException если {@link CityBuilder#population} принимает недопустимое значение (см. описание {@link CityBuilder#population})
     */
    public CityBuilder setPopulation(String population) throws ValueException {
        try {
            Long p = null;
            if (!population.equals("")) {
                p = Long.parseLong(population);
                if (p <= 0) {
                    throw new ValueException("Значение население должно быть больше 0");
                }
            }
            this.population = p;
        } catch (NumberFormatException e) {
            throw new ValueException("Значение населения должно быть числом в диапазоне от 1 до 9 223 372 036 854 775 807");
        }
        return this;
    }
    /**
     * @param metersAboveSeaLevel значение высоты над уровнем моря
     * @return {@link CityBuilder} с заданным {@link CityBuilder#metersAboveSeaLevel}
     * @throws ValueException если {@link CityBuilder#metersAboveSeaLevel} принимает недопустимое значение (см. описание {@link CityBuilder#metersAboveSeaLevel})
     */
    public CityBuilder setMetersAboveSeaLevel(String metersAboveSeaLevel) throws ValueException {
        try {
            this.metersAboveSeaLevel = Integer.parseInt(metersAboveSeaLevel);
        } catch (NumberFormatException e) {
            throw new ValueException("Значение высоты над уровнем моря должно быть целым числом в диапазоне от -2147483647 до 2147483647");
        }
        return this;
    }
    /**
     * @param climate климат
     * @return {@link CityBuilder} с заданным {@link CityBuilder#climate}
     * @throws ValueException если {@link CityBuilder#climate} принимает недопустимое значение (см. описание {@link CityBuilder#climate})
     */
    public CityBuilder setClimate(String climate) throws ValueException {
        if (climate == null || climate.equals("")) {
            throw new ValueException("Поле климат должно иметь значение");
        }
        try {
            this.climate = Climate.valueOf(climate);
        } catch (IllegalArgumentException e) {
            throw new ValueException("Поле климат должно соответствовать одному из предложенных значений");
        }
        return this;
    }
    /**
     * @param government власть
     * @return {@link CityBuilder} с заданным {@link CityBuilder#government}
     * @throws ValueException если {@link CityBuilder#government} принимает недопустимое значение (см. описание {@link CityBuilder#government})
     */
    public CityBuilder setGovernment(String government) throws ValueException {
        if (government == null || government.equals("")) {
            throw new ValueException("Поле власть должно иметь значение");
        }
        try {
            this.government = Government.valueOf(government);
        } catch (IllegalArgumentException e) {
            throw new ValueException("Поле власть должно соответствовать одному из предложенных значений");
        }
        return this;
    }
    /**
     * @param standardOfLiving уровень жизни
     * @return {@link CityBuilder} с заданным {@link CityBuilder#standardOfLiving}
     * @throws ValueException если {@link CityBuilder#standardOfLiving} принимает недопустимое значение (см. описание {@link CityBuilder#standardOfLiving})
     */
    public CityBuilder setStandardOfLiving(String standardOfLiving) throws ValueException {
        if (standardOfLiving == null || standardOfLiving.equals("")) {
            throw new ValueException("Поле уровень жизни должно иметь значение");
        }
        try {
            this.standardOfLiving = StandardOfLiving.valueOf(standardOfLiving);
        } catch (IllegalArgumentException e) {
            throw new ValueException("Поле уровень жизни должно соответствовать одному из предложенных значений");
        }
        return this;
    }
    /**
     * @param height значение роста мэра
     * @return {@link CityBuilder} с заданным {@link CityBuilder#governor}
     * @throws ValueException если рост меньше 0 или вовсе не является целым числом
     */
    public CityBuilder setGovernor(String height) throws ValueException {
        try {
            long h = Long.parseLong(height);
            if (h <= 0) {
                throw new ValueException("Рост мэра должен быть больше 0");
            }
            this.governor = new Human(h);
        } catch (NumberFormatException e) {
            throw new ValueException("Значение роста должно быть целым числом в диапазоне от 1 до 2147483647");
        }
        return this;
    }

    /**
     * @return объект {@link City} с полями из {@link CityBuilder}.
     */
    public City GetCity() {
        return new City(id, name, coordinates, creationDate, area, population, metersAboveSeaLevel, climate, government, standardOfLiving, governor);
    }
}
