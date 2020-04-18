package zzleep.core.data;

import org.springframework.stereotype.Component;
import zzleep.core.settings.DataConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Context {

    private Connection connection;

    private static Context instance = new Context();

    private Context() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DataConfig.getUrl(), DataConfig.getUser(), DataConfig.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Context getInstance() {
        return instance;
    }

    public DataResponse insert(String table, String columns, String values) {
        String sql = String.format("set search_path = 'zzleep';insert into %s (%s) values (%s);", table, columns, values);
        try {
            connection.createStatement().execute(sql);
            return DataResponse.OK;
        } catch (SQLException e) {
            e.printStackTrace();
            return DataResponse.ERROR;
        }
    }

    public <TType> List<TType> select(
            String tableName,
            String condition,
            ResultSetExtractor<TType> extractor
    ) {
        List<TType> result = new ArrayList<>();
        String sql = String.format("select * from %s where %s;", tableName, condition);
        try {
            Statement statement = connection.createStatement();
            statement.execute("set search_path = 'zzleep';");
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next())
                result.add(extractor.extract(resultSet));
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public <TType> List<TType> select(
            String tableName,
            ResultSetExtractor<TType> extractor
    ) {
        return select(tableName, "1 = 1", extractor);
    }


    public interface ResultSetExtractor<TType> {
        TType extract(ResultSet resultSet) throws SQLException;
    }

}
