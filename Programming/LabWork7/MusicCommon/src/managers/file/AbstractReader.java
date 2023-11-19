package managers.file;


import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;
import result.Result;
import java.io.Serializable;

public abstract class AbstractReader<T extends Comparable<T> & IDAccess> implements Serializable  {
    protected Collection<T> collection;
    protected String source;
    protected LoadDescription<T> load_description;

    public AbstractReader(String source, LoadDescription<T> load_description, Collection<T> collection) throws NullPointerException {

        this.load_description = load_description;
        this.collection = collection;

        if (source == null) {
            throw new NullPointerException("source name is not set");
        }

        this.source = source;
    }

    public abstract Result<Collection<T>> read();
}
