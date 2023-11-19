package managers.file.decorators.DataBase;

import common.LocalizationKeys;
import managers.file.AbstractReader;
import managers.file.DBSavable;
import managers.file.decorators.ReaderDecorator;
import common.Collection;
import common.IDAccess;
import common.descriptions.LoadDescription;
import managers.BaseTextReceiver;
import result.Result;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBReader<T extends Comparable<T> & IDAccess & DBSavable> extends ReaderDecorator<T> {
    public DBReader(String TableName, LoadDescription<T> load_description, AbstractReader<T> reader, Collection<T> collection) throws FileNotFoundException, NullPointerException, SecurityException {
        super(TableName, load_description, reader, collection);
    }

    @Override
    public Result<Collection<T>> read() {
        try {
            String db_url = System.getenv("DB_URL"); // configure connection params
            Properties info = new Properties();
            info.load(new FileInputStream("db.cfg"));
            Connection conn = DriverManager.getConnection(db_url, info); // starting connection

            PreparedStatement stat = conn.prepareStatement("select * from " + '"' + source + '"' + ';'); // preparing query

            DBLoader<T> loader = new DBLoader<>(new BaseTextReceiver() {
                @Override
                public void print(String message) {
                }

                @Override
                public void println(String message) {
                }
            }, stat.executeQuery()); // getting answer and instantly give it to DBLoader

            collection = loader.load(load_description); // filling the collection

            stat.close();
            conn.close();
            return Result.success(collection);
        } catch (SQLTimeoutException e){
            return Result.failure(e, LocalizationKeys.ERROR_DATABASE_ANSWER);
        } catch (SQLException e){
            return Result.failure(e, LocalizationKeys.ERROR_REQUEST);
        } catch (FileNotFoundException e) {
            return Result.failure(e, LocalizationKeys.FILE_NOT_FOUND_DATABASE);
        } catch (SecurityException e) {
            return Result.failure(e, LocalizationKeys.NOT_PERMISSION_FOR_FILE_FOR_DATABASE);
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            return Result.failure(e, LocalizationKeys.ERROR_WITH_FILE_FOR_DATABASE);
        } catch (ArrayIndexOutOfBoundsException e){
            return Result.failure(e, LocalizationKeys.ERROR_WITH_DATABASE_ENUM);
        }
    }
}
