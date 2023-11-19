package managers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractParser {

    private static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();

    static {
        PARSERS.put(Integer.class, Integer::valueOf);
        PARSERS.put(Long.class, Long::valueOf);
        PARSERS.put(Float.class, Float::valueOf);
        PARSERS.put(Double.class, Double::valueOf);
        PARSERS.put(Byte.class, Byte::valueOf);
        PARSERS.put(Short.class, Short::valueOf);
        PARSERS.put(Character.class, s -> s.charAt(0));
        PARSERS.put(Boolean.class, Boolean::valueOf);
        PARSERS.put(LocalDate.class, (String s)->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                return LocalDate.parse(s, formatter);
            }catch (Exception e){
                throw new IllegalArgumentException("Date format is not correct. Use dd-MM-yyyy");
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T parse(String s, Class<?> type){
        if (isWrapper(type) || type.equals(LocalDate.class)) {
            return (T) parseWrapper(s, type);
        } else if (type.equals(String.class)) {
            return (T) s;
        } else if (type.isEnum()){
            return (T) parseEnum(s, (Class<Enum>) type);
        } else {
            return (T) parseComposite(s, type);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T parseWrapper(String s, Class<T> type){
        return (T) PARSERS.get(type).apply(s);
    }

    public <T extends Enum<T>> Enum<T> parseEnum(String s, Class<T> type){
        return Enum.valueOf(type, s);
    }

    public abstract <T> T parseComposite(String s, Class<T> type);

    private boolean isWrapper(Class<?> type) {
        return type.equals(Integer.class) || type.equals(Long.class) || type.equals(Double.class)
                || type.equals(Float.class) || type.equals(Short.class) || type.equals(Byte.class)
                || type.equals(Character.class) || type.equals(Boolean.class);
    }

}
