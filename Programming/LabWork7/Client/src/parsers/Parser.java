package parsers;

import managers.AbstractParser;

public class Parser extends AbstractParser {

    @Override
    public <T> T parseComposite(String s, Class<T> aClass) {
        throw new RuntimeException("Can't parse composite from console! Incorrect LoadDescription!");
    }
}
