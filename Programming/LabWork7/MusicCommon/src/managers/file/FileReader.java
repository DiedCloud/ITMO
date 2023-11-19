package managers.file;

import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;
import result.Result;

import java.io.FileNotFoundException;

public class FileReader<T extends Comparable<T> &IDAccess> extends AbstractFileReader<T> {
    public FileReader(String fileName, LoadDescription<T> load_description, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(fileName, load_description, collection);
    }

    @Override
    public Result<Collection<T>> read() {
        System.out.println("Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");
        return null;
    }
}
