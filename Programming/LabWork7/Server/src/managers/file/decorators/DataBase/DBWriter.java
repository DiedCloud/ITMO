package managers.file.decorators.DataBase;

import common.descriptions.LoadDescription;
import main.Main;
import managers.file.AbstractWriter;
import managers.file.DBSavable;
import managers.file.decorators.WriterDecorator;
import common.IDAccess;
import result.Result;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class DBWriter<T extends Comparable<T> & IDAccess & DBSavable> extends WriterDecorator<T> {
    Connection connection;
    LoadDescription<T> descriptionForUpdate;

    public DBWriter(String destination, AbstractWriter<T> writer, LoadDescription<T> descriptionForUpdate) throws SQLException, IOException, NullPointerException, SecurityException {
        super(destination, writer);
        String db_url = System.getenv("DB_URL"); // configure connection params
        Properties info = new Properties();
        info.load(new FileInputStream("db.cfg"));
        connection = DriverManager.getConnection(db_url, info); // starting connection

        this.descriptionForUpdate = descriptionForUpdate;
    }

    @Override
    public void write() throws Exception {
        PreparedStatement encodingStat = connection.prepareStatement("SET client_encoding TO 'UTF8';");
        encodingStat.execute();
        connection.setAutoCommit(false);
        connection.setSavepoint();

        //clearing
        PreparedStatement stat = connection.prepareStatement("truncate table ?");
        stat.setString(1, destination);
        stat.execute();

        //saving
        stat = connection.prepareStatement("insert into ? values (?);");
        stat.setString(1, destination);

        String collectionRows = "";
        Iterator<T> iter = collection.getCollection().iterator();
        while (iter.hasNext()) {
            Result<List<String>> row = iter.next().toFields();
            if (row.isSuccess()) {
                collectionRows += String.join(", ", row.getValue().get());
                if (iter.hasNext()) {
                    collectionRows += ", ";
                }
            } else {
                Main.logger.error("Error in some element while saving collection. " + row.getMessage());
                connection.rollback();
                connection.setAutoCommit(true);
                throw new Exception(row.getMessage());
            }
        }

        stat.setString(2, collectionRows);
        if (stat.execute()) {
            Main.logger.info("Collection saved");
            connection.commit();
        } else {
            Main.logger.error("Error with data base. Collection not saved");
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Override
    public Result<Boolean> insert(T obj) {
        Statement stat = null;
        try {
            String sql = "";
            stat = connection.createStatement();

            Result<List<String>> row = obj.toFields();
            if (row.isSuccess()) {
                List<String> columns = getFieldsFromDescription(descriptionForUpdate);

                String comSepColumns = "";
                if (columns.size() == row.getValue().orElse(new ArrayList<>()).size()) {
                    for (int j=0; j < columns.size(); j+=1){
                        comSepColumns += '"' + columns.get(j) + '"';
                        if (j < columns.size()-1)
                            comSepColumns += ", ";
                    }
                }

                sql = "insert into \"" + destination + "\" ("+ comSepColumns +")" +
                      " values (" + String.join(", ", row.getValue().orElse(new ArrayList<>())) + ");";
                Main.logger.info("Запрос к бд " + sql);
            } else {
                Main.logger.error("Problem with getting field of element. Can not be save in data base. " + row.getMessage());
                return Result.failure(null, "Problem with getting field of element. Can not be save in data base. " + row.getMessage());
            }
            return Result.success(stat.execute(sql));
        } catch (SQLException e) {
            Main.logger.error("Error with data base. Element not saved. " + e.getMessage());
            return Result.failure(e, "Error with data base. Element not saved. " + e.getMessage());
        }
    }

    private List<String> getFieldsFromDescription(LoadDescription<?> d){
        List<String> res = new ArrayList<>();
        for (LoadDescription<?> i: d.getFields()){
            if (i.getFields().isEmpty())
                res.add(i.getFieldName());
            else
                res.addAll(getFieldsFromDescription(i));
        }
        return res;
    }

    @Override
    public Result<Boolean> update(T obj, long id) {
        Statement stat = null;
        try {
            Result<List<String>> row = obj.toFields();
            List<String> columns = getFieldsFromDescription(descriptionForUpdate);
            String setter = "";
            if (row.isSuccess() && columns.size() == row.getValue().orElse(new ArrayList<>()).size()) {
                for (int j=0; j < columns.size(); j+=1){
                    setter += '"' + columns.get(j) + '"' + " = " + row.getValue().get().get(j);
                    if (j < columns.size()-1)
                        setter += ", ";
                }
            } else {
                Main.logger.error("Problem with getting field of element. Can not be save in data base. " + row.getMessage());
                return Result.failure(null, "Problem with getting field of element. Can not be save in data base. " + row.getMessage());
            }
            stat = connection.createStatement();
            return Result.success(stat.execute("update \"" + destination + "\" set " + setter + " where id = " + id + ";"));
        } catch (SQLException e) {
            Main.logger.error("Error with data base. Element not updated. " + e.getMessage());
            return Result.failure(e, "Error with data base. Element not updated. " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> remove(long id){
        PreparedStatement stat = null;
        try {
            stat = connection.prepareStatement("delete from \"" + destination + "\" where id = ?;");
            stat.setLong(1, id);

            return Result.success(stat.execute());
        } catch (SQLException e) {
            Main.logger.error("Error with data base. Element not removed. " + e.getMessage());
            return Result.failure(e, "Error with data base. Element not removed. " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> remove(String col, String val){
        PreparedStatement stat = null;
        try {
            stat = connection.prepareStatement("delete from \"" + destination + "\" where \"" + col + "\" = ?;");
            stat.setString(1, val);
            return Result.success(stat.execute());
        } catch (SQLException e) {
            Main.logger.error("Error with data base. Element not removed. " + e.getMessage());
            return Result.failure(e, "Error with data base. Element not removed. " + e.getMessage());
        }
    }
}
