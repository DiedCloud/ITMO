package client;

import Exceptions.ValueException;
import commands.*;
import storage.*;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Класс содержащий методы для обработки аргументов введенной команды
 * @author Фролов К.Д.
 */
public class Parser {

    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Help}, если количество аргументов и сами аргументы верные
     */
    static Help Help(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new Help();
        } else {
            System.out.println("У команды help нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Info}, если количество аргументов и сами аргументы верные
     */
    static Info Info(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new Info();
        } else {
            System.out.println("У команды info нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Show}, если количество аргументов и сами аргументы верные
     */
    static Show Show(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new Show();
        } else {
            System.out.println("У команды show нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}er
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Insert}, если количество аргументов и сами аргументы верные
     */
    static Insert Insert(String[] c, Scanner reader) {
        Insert commandObj = null;
        if (c.length == 2) {
            try {
                Long key = Long.parseLong(c[1]);
                City obj = CreateCity(reader);
                commandObj = new Insert(key, obj);
            } catch (NumberFormatException e) {
                System.out.println("Ключ должен быть целым числом");
            } catch (ValueException | DateTimeParseException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("У команды insert только один аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Update}, если количество аргументов и сами аргументы верные
     */
    static Update Update(String[] c, Scanner reader) {
        Update commandObj = null;
        if (c.length == 2) {
            try {
                long key = Long.parseLong(c[1]);
                City obj = CreateCity(reader);
                commandObj = new Update(key, obj);
            } catch (NumberFormatException e) {
                System.out.println("Ключ должен быть целым числом");
            } catch (ValueException | DateTimeParseException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка ввода");
            }
        } else {
            System.out.println("У команды update только один аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link RemoveKey}, если количество аргументов и сами аргументы верные
     */
    static RemoveKey RemoveKey(String[] c, Scanner reader) {
        RemoveKey commandObj = null;
        if (c.length == 2) {
            try {
                Long key = Long.parseLong(c[1]);
                commandObj = new RemoveKey(key);
            } catch (NumberFormatException e) {
                System.out.println("Ключ должен быть целым числом");
            }
        } else {
            System.out.println("У команды remove_key только один аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Clear}, если количество аргументов и сами аргументы верные
     */
    static Clear Clear(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new Clear();
        } else {
            System.out.println("У команды clear нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Save}, если количество аргументов и сами аргументы верные
     */
    static Save Save(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new Save();
        } else {
            System.out.println("У команды save нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link Exit}, если количество аргументов и сами аргументы верные
     */
    static Exit Exit(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new Exit();
        } else {
            System.out.println("У команды exit нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link RemoveGreater}, если количество аргументов и сами аргументы верные
     */
    static RemoveGreater RemoveGreater(String[] c, Scanner reader) {
        RemoveGreater commandObj = null;
        if (c.length == 1) {
            try {
                City obj;
                obj = CreateCity(reader);
                commandObj = new RemoveGreater(obj);
            } catch (ValueException | DateTimeParseException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка ввода");
            }
        } else {
            System.out.println("У команды remove_greater нет аргументов. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link ReplaceIfGreater}, если количество аргументов и сами аргументы верные
     */
    static ReplaceIfGreater ReplaceIfGreater(String[] c, Scanner reader) {
        ReplaceIfGreater commandObj = null;
        if (c.length == 2) {
            try {
                Long key = Long.parseLong(c[1]);
                City obj = CreateCity(reader);
                commandObj = new ReplaceIfGreater(key, obj);
            } catch (NumberFormatException e) {
                System.out.println("Ключ должен быть целым числом");
            } catch (ValueException | DateTimeParseException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка ввода");
            }
        } else {
            System.out.println("У команды replace_if_greater только один аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link FilterContainsName}, если количество аргументов и сами аргументы верные
     */
    static FilterContainsName FilterContainsName(String[] c, Scanner reader) {
        FilterContainsName commandObj = null;
        if (c.length == 2) {
            String name = c[1];
            commandObj = new FilterContainsName(name);
        } else {
            System.out.println("У команды filter_contains_name только один аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link FilterStartsWithName}, если количество аргументов и сами аргументы верные
     */
    static FilterStartsWithName FilterStartsWithName(String[] c, Scanner reader) {
        FilterStartsWithName commandObj = null;
        if (c.length == 2) {
            String name = c[1];
            commandObj = new FilterStartsWithName(name);
        } else {
            System.out.println("У команды filter_starts_with_name только один аргумент. Передано " + (c.length - 1));
        }
        return commandObj;
    }
    /**
     * Метод преобразует массив из названия команды на 0-й позиции и аргументами на оставшихся в объект {@link Command}
     * @param c массив с названием команды и ее аргументами
     * @param reader Текущий поток, из которого читается последовательность команд. Нужно командам с многострочным вводом.
     * @return Объект команды {@link PrintUniqueMetersAboveSeaLevel}, если количество аргументов и сами аргументы верные
     */
    static PrintUniqueMetersAboveSeaLevel PrintUniqueMetersAboveSeaLevel(String[] c, Scanner reader) {
        if (c.length == 1) {
            return new PrintUniqueMetersAboveSeaLevel();
        } else {
            System.out.println("У команды print_unique_meters_above_sea_level нет аргументов. Передано " + (c.length - 1));
            return null;
        }
    }

    /**
     * Метод, реализующий ввод объекта {@link City}. Многострочный ввод команды.
     * @param reader текущий поток, из которого читается последовательность команд.
     * @return Собранный объект {@link City}
     * @throws ValueException Если какой то из параметров введен неправильно, из соответствующего метода {@link CityBuilder} выброситься это исключение с описанием проблемы
     * @throws DateTimeParseException Если нарушен формат ввода даты. Выбрасывается из {@link CityBuilder#setCreationDate(String)}
     */
    protected static City CreateCity(Scanner reader) throws ValueException, DateTimeParseException {
        CityBuilder cityBuilder = new CityBuilder();
        String s = "";

        cityBuilder.setID(System.currentTimeMillis());

        System.out.print("Name: ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setName(s);

        System.out.print("Coordinate X (целое число от -2147483647 до 2147483647): ");
        s = reader.nextLine();
        System.out.println(s);
        String s2 = "";
        System.out.print("Coordinate Y (целое число от -804 до 2147483647): ");
        s2 = reader.nextLine();
        System.out.println(s2);
        cityBuilder.setCoordinates(s, s2);

        cityBuilder.setCreationDate(java.time.LocalDateTime.now().toString());

        System.out.print("Area (число от -3.4*10^38 до 3.4*10^38): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setArea(s);

        System.out.print("Population (целое число от 1 до 9 223 372 036 854 775 807): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setPopulation(s);

        System.out.print("Meters above sea level (целое число от -2147483647 до 2147483647): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setMetersAboveSeaLevel(s);

        System.out.print("Climate (RAIN_FOREST, HUMIDSUBTROPICAL, MEDITERRANIAN): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setClimate(s);

        System.out.print("Government (DICTATORSHIP, PUPPET_STATE, OLIGARCHY): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setGovernment(s);

        System.out.print("Standard of living (ULTRA_HIGH, VERY_HIGH, HIGH, NIGHTMARE): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setStandardOfLiving(s);

        System.out.print("Governor height (целое число от 1 до 2147483647): ");
        s = reader.nextLine();
        System.out.println(s);
        cityBuilder.setGovernor(s);

        return cityBuilder.GetCity();
    }
}
