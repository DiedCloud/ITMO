package managers.file.decorators.CSV;

import common.MusicBand;
import managers.file.CSVSavable;
import managers.file.decorators.ReaderDecorator;
import common.Collection;
import common.IDAccess;
import managers.file.AbstractFileReader;
import common.descriptions.LoadDescription;
import managers.BaseTextReceiver;
import result.Result;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVReader<T extends Comparable<T> & IDAccess & CSVSavable> extends ReaderDecorator<T> {
    public CSVReader(String fileName, LoadDescription<T> load_description, AbstractFileReader<T> reader, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(fileName, load_description, reader, collection);
    }

    @Override
    public Result<Collection<T>> read() {
        BufferedReader buffered_reader = null;
        String line;
        try {
            buffered_reader = new BufferedReader(new FileReader(file));
            while (true) {
                line = buffered_reader.readLine();
                if (!(line == null || line.equals(""))) {
                    CSVLoader loader = new CSVLoader(new BaseTextReceiver() {
                        @Override
                        public void print(String message) {
                        }

                        @Override
                        public void println(String message) {
                        }
                    }, line);
                    T element = (T) loader.enter(load_description).getValue();
                    Result<?> res = collection.add((T) element);
                    if (!res.isSuccess()) {
                        buffered_reader.close();
                        return Result.failure(res.getError().get(), res.getMessage());
                    }
                } else {
                    break;
                }
            }
            buffered_reader.close();
            return Result.success(collection);
        } catch (IndexOutOfBoundsException e) {
            return Result.failure(e, "Файл с коллекцией не соответствует структуре хранимых объектов");
        } catch (FileNotFoundException e) {
            return Result.failure(e, "Ошибка при открытии файла");
        } catch (Exception e) {
            return Result.failure(e, "Ошибка валидации объекта");
        }
    }
}
