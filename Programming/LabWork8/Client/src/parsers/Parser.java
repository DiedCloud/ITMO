package parsers;

import managers.AbstractParser;

public class Parser extends AbstractParser {

    @Override
    public <T> T parseComposite(String s, Class<T> aClass) {
        throw new RuntimeException("ERROR_IMPOSSIBLE_PARSE_COMPOSITE_FROM_CONSOLE");
    }

    @Override
    public <T extends Enum<T>> Enum<T> parseEnum(String s, Class<T> type) {
        try {
            return Enum.valueOf(type, s);
        }catch (IllegalArgumentException e){
            throw new RuntimeException("ERROR_WRONG_ENUM_VALUE");
        }
    }

}
