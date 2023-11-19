package managers.file;

import common.Collection;
import common.IDAccess;

import java.io.*;

public abstract class AbstractFileWriter<T extends Comparable<T> & IDAccess> implements Serializable {
    protected Collection<T> collection;
    protected File file;
    public AbstractFileWriter(String fileName) throws IOException, NullPointerException, SecurityException {

        if (fileName == null) {
            throw new NullPointerException("FILE_NAME is not set");
        }

        file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " does not exist");
        }

        if (!file.canWrite()) {
            throw new SecurityException("File " + fileName + " is not writable");
        }
    }

    public void setCollection(Collection<T> collection){
        this.collection = collection;
    }
    public abstract void write() throws Exception;
}
