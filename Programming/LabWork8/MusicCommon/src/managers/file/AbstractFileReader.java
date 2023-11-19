package managers.file;


import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

public abstract class AbstractFileReader<T extends Comparable<T> & IDAccess> extends AbstractReader<T> implements Serializable {
    protected File file;

    public AbstractFileReader(String fileName, LoadDescription<T> load_description, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(fileName, load_description, collection);

        file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " does not exist");
        }

        if (!file.canRead()) {
            throw new SecurityException("File " + fileName + " is not readable");
        }
    }
}
