package managers.file.decorators;

import common.Collection;
import common.IDAccess;
import managers.file.AbstractFileReader;
import common.descriptions.LoadDescription;

import java.io.FileNotFoundException;

public abstract class ReaderDecorator<T extends Comparable<T> & IDAccess> extends AbstractFileReader<T> {
    AbstractFileReader<T> reader;


    public ReaderDecorator(String fileName, LoadDescription<T> load_description, AbstractFileReader<T> reader, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(fileName, load_description, collection);
        this.reader = reader;
    }
}
