package managers.file;

import common.IDAccess;
import result.Result;

import java.io.IOException;

public class FileWriter<T extends Comparable<T> & IDAccess> extends AbstractFileWriter<T> {
    public FileWriter(String fileName) throws IOException, NullPointerException, SecurityException {
        super(fileName);
    }

    @Override
    public void write() {
        System.out.println("Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");
    }

    public Result<Boolean> insert(T obj) {
        return Result.failure(null, "Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");

    }

    public Result<Boolean> update(T obj, long id) {
        return Result.failure(null, "Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");

    }

    public Result<Boolean> remove(long id) {
        return Result.failure(null, "Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");
    }

    public Result<Boolean> remove(String col, String val) {
        return Result.failure(null, "Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");
    }
}
