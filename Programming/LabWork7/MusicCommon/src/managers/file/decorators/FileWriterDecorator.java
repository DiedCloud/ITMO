package managers.file.decorators;

import common.IDAccess;
import managers.file.AbstractFileWriter;
import managers.file.AbstractWriter;

import java.io.IOException;

public abstract class FileWriterDecorator<T extends Comparable<T> & IDAccess> extends AbstractFileWriter<T> {
    AbstractWriter<T> writer;

    public FileWriterDecorator(String fileName, AbstractWriter<T> writer) throws IOException, NullPointerException, SecurityException {
        super(fileName);
        this.writer = writer;
    }
}
