package managers.file.decorators;

import common.IDAccess;
import managers.file.AbstractWriter;

import java.io.IOException;

public abstract class WriterDecorator<T extends Comparable<T> & IDAccess> extends AbstractWriter<T> {
    AbstractWriter<T> writer;

    public WriterDecorator(String destination, AbstractWriter<T> writer) throws IOException, NullPointerException, SecurityException {
        super(destination);
        this.writer = writer;
    }
}
