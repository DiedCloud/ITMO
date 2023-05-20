package storage;

import Exceptions.ValueException;
import commands.Response;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Класс, хранящий коллекцию и выполняющий все операции с ней. Принимает запросы на выполнение команды. Реализует паттерн одиночка.
 * @author Фролов К.Д.
 */
public class Receiver {
    /**
     * коллекция, хранящая города
     */
    private Hashtable<Long, City> collection;
    /**
     * Дата инициализации коллекции
     */
    private java.time.LocalDateTime initDate;
    /**
     * путь к csv файлу, из которого загружены данные в коллекцию в начале работы и будут сохраняться при команде {@link Receiver#save()}
     */
    private String fileName;
    /**
     * Ссылка на текущий {@link Receiver}. Класс реализует паттерн одиночка.
     */
    private static Receiver instance;

    /**
     * Здесь происходит вся инициализация: инициализация коллекции, подключение к файлу, его чтение и запись данных в коллекцию. Происходит одна один раз - спасибо паттерну одиночка.
     */
    private Receiver() {
        fileName = System.getenv("FILE_NAME");
        collection = new Hashtable<>();
        initDate = LocalDateTime.now();

        File file = null;
        try {
            file = new File(fileName);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        Scanner reader = null;
        try {
            reader = new Scanner(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (SecurityException e) {
            System.out.println("Недостаточно прав для чтения файла");
        }

        while (reader.hasNextLine()) {
            try {
                String in = reader.nextLine();
                if (in == null || in.equals("")) {
                    continue;
                }
                String[] c = in.trim().split(",");
                if (c.length != 12) {
                    System.out.println("Количество элементов в строке не соответствует. Строка пропущена");
                    continue;
                }
                long id = Long.parseLong(c[0]);
                if (isIdUnique(id)) {
                    City city = new CityBuilder()
                            .setID(id)
                            .setName(c[1])
                            .setCoordinates(c[2], c[3])
                            .setCreationDate(c[4])
                            .setArea(c[5])
                            .setPopulation(c[6])
                            .setMetersAboveSeaLevel(c[7])
                            .setClimate(c[8])
                            .setGovernment(c[9])
                            .setStandardOfLiving(c[10])
                            .setGovernor(c[11]).GetCity();
                    collection.put(id, city);
                } else {
                    System.out.println("Город с таким id уже есть. Строка пропущена.");
                }
            } catch (ValueException | NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Неправильный формат даты. ");
            }
        }
        System.out.println("Итого элементов в коллекции - " + collection.size());
    }

    /**
     * Одиночка. Если объекта нет - создадим, иначе вернем тот, что есть.
     * @return ссылка на существующий объект {@link Receiver}
     */
    public static Receiver GetInstance() {
        if (instance == null) {
            instance = new Receiver();
        }
        return instance;
    }

    /**
     * Проверка на отсутствие данного id в коллекции. Нужно для добавления новых городов при чтении из файла или в {@link Receiver#insert(Long, City)}
     * @param id {@link City#id}
     * @return true если такого {@link City#id} еще нет в {@link Receiver#collection}
     * @see City
     */
    private boolean isIdUnique(long id) {
        for (City c : collection.values()) {
            if (id == c.getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает информацию о коллекции (тип, дата инициализации, количество элементов)
     * @return объект {@link Response} с ответом
     */
    public Response<String> info() {
        return new Response<String>("", "Тип коллекции: " + collection.getClass().getName() +
                "\nКоличество элементов: " + collection.size() +
                "\nДата инициализации: " + initDate);
    }

    /**
     * Возвращает все элементы коллекции в строковом представлении
     * @return объект {@link Response} с ответом
     */
    public Response<String> show() {
        String res = "";
        for (Long c : collection.keySet()) {
            res += c + ":\t" + collection.get(c).toString() + "\n";
        }
        return new Response<String>("", res);
    }

    /**
     * Добавляет новый элемент с заданным ключом
     * @param key ключ, которому будет в коллекции соответствовать объект
     * @param obj объект, который нужно положить в коллекцию
     * @return объект {@link Response} с информацией об ошибке или добавленным объектом при успехе
     */
    public Response<City> insert(Long key, City obj) {
        if (!isIdUnique(obj.getId())) {
            return new Response<City>("В коллекции уже лежит город с таким id", obj);
        } else if (collection.containsKey(key)) {
            return new Response<City>("Этот ключ занят", obj);
        }
        collection.put(key, obj);
        return new Response<City>("", obj);
    }

    /**
     * Обновляет значение элемента коллекции, {@link City#id} которого равен заданному
     * @param id {@link City#id}, по которому будет обновлен объект
     * @param obj новый объект, который нужно положить в коллекцию
     * @return объект {@link Response} с информацией об ошибке или сообщением об успехе
     */
    public Response<String> update(long id, City obj) {
        for (Long i : collection.keySet()) {
            if ((collection.get(i)).getId() == id) {
                collection.replace(i, obj);
                return new Response<String>("", "Success");
            }
        }
        return new Response<String>("Такого id нет", "");
    }

    /**
     * удаляет элемент из коллекции по его ключу
     * @param key ключ, который нужно удалить
     * @return объект {@link Response} с информацией об ошибке или удаленным ключом при успехе
     */
    public Response<Long> remove_key(Long key) {
        if (collection.remove(key) != null) {
            return new Response<Long>("", key);
        }
        return new Response<Long>("Такого ключа нет", key);
    }

    /**
     * очистить коллекцию
     * @return объект {@link Response} с сообщением об успехе
     */
    public Response<String> clear() {
        collection.clear();
        return new Response<String>("", "Cleared");
    }

    /**
     * сохранить коллекцию в файл
     * @return объект {@link Response} с информацией об ошибке или сообщением об успехе
     * @see Receiver#fileName
     */
    public Response<String> save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (City c : collection.values()) {
                writer.write(c.toCommaRow() + "\n");
            }
            writer.close();
            return new Response<>("", "saved");
        } catch (IOException e) {
            return new Response<>(e.getMessage(), "");
        }
    }

    /**
     * удаляет из коллекции все элементы, превышающие заданный
     * @param element объект {@link City}, с которым будут сравнивать объекты коллекции
     * @return объект {@link Response} с информацией об ошибке или {@link ArrayList<Long>}, преобразованном к строке, с удаленными ключами при успехе
     */
    public Response<String> remove_greater(City element) {
        ArrayList<Long> a = new ArrayList<>();
        for (Long i : collection.keySet()) {
            if ((collection.get(i)).compareTo(element) > 0) {
                a.add(i);
            }
        }
        for (Long i : a) {
            collection.remove(i);
        }
        return new Response<String>("", a.toString());
    }

    /**
     * заменяет значение по ключу, если новое значение больше старого
     * @param key ключ. Объект, соответствующий ему, будет сравниваться и, возможно, заменяться
     * @param obj новый объект {@link City}, который нужно положить в коллекцию, если он больше
     * @return объект {@link Response} с информацией об ошибке или сообщением об успехе
     */
    public Response<String> replace_if_greater(Long key, City obj) {
        if (collection.containsKey(key)) {
            if (obj.compareTo(collection.get(key)) > 0) {
                collection.replace(key, obj);
                return new Response<String>("", "Заменено");
            }
            else{
                return new Response<String>("", "переданный объект меньше");
            }
        }
        else{
            return new Response<String>("Нет такого ключа", "");
        }
    }

    /**
     * Возвращает элементы в строковом представлении, значение поля {@link City#name} которых содержит заданную подстроку
     * @param name подстрока
     * @return объект {@link Response} с ответом
     */
    public Response<String> filter_contains_name(String name) {
        String res = "";
        for (City c : collection.values()) {
            if (c.getName().contains(name)) {
                res += c.toString() + "\n";
            }
        }
        return new Response<String>("", res);
    }

    /**
     * Возвращает элементы в строковом представлении, значение поля {@link City#name} которых начинается с заданной подстроки
     * @param name подстрока
     * @return объект {@link Response} с ответом
     */
    public Response<String> filter_starts_with_name(String name) {
        String res = "";
        for (City c : collection.values()) {
            if (c.getName().startsWith(name)) {
                res += c.toString() + "\n";
            }
        }
        return new Response<String>("", res);
    }

    /**
     * Возвращает уникальные значения поля metersAboveSeaLevel всех элементов в коллекции
     * @return объект {@link Response} с ответом в виде строкового представления {@link HashSet<Integer>}
     */
    public Response<String> print_unique_meters_above_sea_level() {
        HashSet<Integer> res = new HashSet<>();
        for (City c : collection.values()) {
            res.add(c.getMetersAboveSeaLevel());
        }
        return new Response<String>("", res.toString());
    }
}
