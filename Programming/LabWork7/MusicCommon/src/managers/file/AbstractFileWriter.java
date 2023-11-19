package managers.file;

import common.IDAccess;

import java.io.*;

public abstract class AbstractFileWriter<T extends Comparable<T> & IDAccess> extends AbstractWriter<T> implements Serializable {
    protected File file;
    public AbstractFileWriter(String fileName) throws IOException, NullPointerException, SecurityException {
        super(fileName);

        file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " does not exist");
        }

        if (!file.canWrite()) {
            throw new SecurityException("File " + fileName + " is not writable");
        }
    }
}
