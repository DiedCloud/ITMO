package managers.file.decorators;

import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;
import managers.file.AbstractReader;

import java.io.FileNotFoundException;

public abstract class ReaderDecorator<T extends Comparable<T> & IDAccess> extends AbstractReader<T> {
    AbstractReader<T> reader;

    public ReaderDecorator(String source, LoadDescription<T> load_description, AbstractReader<T> reader, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(source, load_description, collection);
        this.reader = reader;
    }
}
