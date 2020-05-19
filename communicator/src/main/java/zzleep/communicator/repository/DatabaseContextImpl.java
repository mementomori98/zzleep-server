package zzleep.communicator.repository;



import zzleep.communicator.settings.DataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseContextImpl implements DatabaseContext {
    private Connection connection;
    private static DatabaseContextImpl instance = new DatabaseContextImpl();

    public DatabaseContextImpl() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(DataConfig.getUrl(), DataConfig.getUser(), DataConfig.getPassword());
        } catch (SQLException | ClassNotFoundException var2) {
            var2.printStackTrace();
        }

    }

    public static DatabaseContextImpl getInstance() {
        return instance;
    }

    @Override
    public void insert(String values) {
        String table = "activeSleep";
        String columns = "";

        String sql  = String.format("set search_path ='zzleep'; insert into %s (%s) values (%s);", table, columns, values);

        try {
            this.connection.createStatement().execute(sql);        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            try {
                throw new Exception("Insertion failed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public ArrayList<String> getActiveSleeps() {
        return null;
    }

    @Override
    public ArrayList<String> getStoppedSleeps() {
        return null;
    }

    @Override
    public ArrayList<String> getStartVentilation() {
        return null;
    }

    @Override
    public ArrayList<String> getStopVentilation() {
        return null;
    }

    private void connect()
    {

    }

    private void closeConnection()
    {

    }

    private void removeActiveSleep(String sleep)
    {

    }
}
