package managers.file;

import common.IDAccess;

import java.io.IOException;

public class FileWriter<T extends Comparable<T> & IDAccess> extends AbstractFileWriter<T> {
    public FileWriter(String fileName) throws IOException, NullPointerException, SecurityException {
        super(fileName);
    }

    @Override
    public void write() {
        System.out.println("Я ничего не умею(( Есть парни из группировки декораторов, каждый из них работает со своим файлом. Обратись к ним.");
    }
}
