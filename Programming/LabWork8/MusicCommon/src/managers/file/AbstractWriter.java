package managers.file;

import common.Collection;
import common.IDAccess;
import result.Result;

import java.io.*;

public abstract class AbstractWriter<T extends Comparable<T> & IDAccess> implements Serializable {
    protected Collection<T> collection;
    protected String destination;
    public AbstractWriter(String destination) throws NullPointerException {

        if (destination == null) {
            throw new NullPointerException("FILE_NAME is not set");
        }

        this.destination = destination;
    }

    public void setCollection(Collection<T> collection){
        this.collection = collection;
    }
    public abstract void write() throws Exception;
    public abstract Result<Boolean> insert(T obj);
    public abstract Result<Boolean> update(T obj, long id);
    public abstract Result<Boolean> remove(long id);
    public abstract Result<Boolean> remove(String col, String val);
}
