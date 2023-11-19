package managers.file.decorators;

import common.IDAccess;
import managers.file.AbstractFileWriter;

import java.io.IOException;

public abstract class WriterDecorator<T extends Comparable<T> & IDAccess> extends AbstractFileWriter<T> {
    AbstractFileWriter<T> writer;

    public WriterDecorator(String fileName, AbstractFileWriter<T> writer) throws IOException, NullPointerException, SecurityException {
        super(fileName);
        this.writer = writer;
    }
}
