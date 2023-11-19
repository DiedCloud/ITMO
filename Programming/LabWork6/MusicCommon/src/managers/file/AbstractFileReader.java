package managers.file;


import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;
import result.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

public abstract class AbstractFileReader<T extends Comparable<T> & IDAccess> implements Serializable {
    protected Collection<T> collection;
    protected File file;
    protected LoadDescription<T> load_description;

    public AbstractFileReader(String fileName, LoadDescription<T> load_description, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        this.load_description = load_description;
        this.collection = collection;

        if (fileName == null) {
            throw new NullPointerException("file name is not set");
        }

        file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " does not exist");
        }

        if (!file.canRead()) {
            throw new SecurityException("File " + fileName + " is not readable");
        }
    }

    public abstract Result<Collection<T>> read();
}
