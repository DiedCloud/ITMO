package managers.file.decorators.CSV;

import main.Main;
import managers.file.CSVSavable;
import managers.file.decorators.FileWriterDecorator;
import common.IDAccess;
import managers.file.AbstractFileWriter;
import result.Result;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter<T extends Comparable<T> & IDAccess & CSVSavable> extends FileWriterDecorator<T> {
    public CSVWriter(String fileName, AbstractFileWriter<T> writer) throws IOException, NullPointerException, SecurityException {
        super(fileName, writer);
    }

    @Override
    public void write() throws Exception {
        BufferedWriter buffered_writer = new BufferedWriter(new FileWriter(destination));
        for (T i: collection.getCollection()){
            Result<String> csv_row = i.toCSV();
            if (csv_row.isSuccess())
                buffered_writer.write(csv_row.getValue().get()+"\n");
            else{
                Main.logger.error("Error while saving collection. " + csv_row.getMessage());
                throw new Exception(String.valueOf(csv_row.getMessage()));}
        }
        buffered_writer.close();
        Main.logger.info("Collection saved");
    }

    @Override
    public Result<Boolean> insert(T t) {
        return null;
    }

    @Override
    public Result<Boolean> update(T t, long i) {
        return null;
    }

    @Override
    public Result<Boolean> remove(long l) {
        return null;
    }

    @Override
    public Result<Boolean> remove(String s, String s1) {
        return null;
    }

}
