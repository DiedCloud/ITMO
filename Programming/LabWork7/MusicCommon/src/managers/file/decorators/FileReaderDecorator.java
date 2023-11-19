package managers.file.decorators;

import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;
import managers.file.AbstractFileReader;
import managers.file.AbstractReader;

import java.io.FileNotFoundException;

public abstract class FileReaderDecorator<T extends Comparable<T> & IDAccess> extends AbstractFileReader<T> {
    AbstractReader<T> reader;

    public FileReaderDecorator(String fileName, LoadDescription<T> load_description, AbstractReader<T> reader, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(fileName, load_description, collection);
        this.reader = reader;
    }
}
