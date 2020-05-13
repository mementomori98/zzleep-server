package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.logging.Logger;
import zzleep.core.settings.DataSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostgresContext implements Context {

    private Connection connection;
    private final Logger logger;

    public PostgresContext(Logger logger) {
        this.logger = logger;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DataSettings.getUrl(), DataSettings.getUser(), DataSettings.getPassword());
        } catch (Exception e) {
            logger.error("Failed to connect to database", e.toString());
        }
    }

    @Override
    public <TType> TType insert(String table, String columns, String values, ResultSetExtractor<TType> extractor) {
        try {
            Statement statement = connection.createStatement();
            ResultSet query = statement.executeQuery(String.format(
                "insert into %s (%s) values (%s) returning *;",
                table, columns, values, columns));
            query.next();
            return extractor.extract(query);
        } catch (Exception e) {
            logger.error(String.format("Error when inserting into: %s(%s) values (%s)",
                table, columns, values), e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <TType> List<TType> select(String table, String condition, ResultSetExtractor<TType> extractor) {
        try {
            List<TType> list = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet query = statement.executeQuery(String.format("select * from %s where %s;", table, condition));
            while (query.next())
                list.add(extractor.extract(query));
            return list;
        } catch (Exception e) {
            logger.error(String.format("Error when selecting from: %s where %s",
                table, condition), e.toString());
            throw new QueryFailedException();
        }

    }

    @Override
    public <TType> List<TType> selectAll(String table, ResultSetExtractor<TType> extractor) {
        return select(table, "true", extractor);
    }

    @Override
    public <TType> TType single(String table, String condition, ResultSetExtractor<TType> extractor) {
        try {
            Statement statement = connection.createStatement();
            ResultSet query = statement.executeQuery(String.format("selectAll * from %s where %s;", table, condition));
            if (!query.next()) return null;
            TType result = extractor.extract(query);
            if (query.next()) throw new QueryContainsMultipleElementsException();
            return result;
        } catch (Exception e) {
            logger.error(String.format("Error when selecting single from %s where %s",
                table, condition), e.toString());
            throw new QueryFailedException();
        }
    }

    @Override
    public <TType> TType update(String table, String fieldsWithValues, String condition, ResultSetExtractor<TType> extractor) {
        try {
            Statement statement = connection.createStatement();
            ResultSet query = statement.executeQuery(String.format(
                "update %s set %s where %s returning *",
                table, fieldsWithValues, condition));
            query.next();
            return extractor.extract(query);
        } catch (Exception e) {
            logger.error(String.format("Error when updating %s %s where %s",
                table, fieldsWithValues, condition), e.toString());
            throw new QueryFailedException();
        }
    }

    @Override
    public void delete(String table, String condition) {
        try {
            connection.createStatement()
                .execute(String.format("delete from %s where %s", table, condition));
        } catch (Exception e) {
            logger.error(String.format("Error when deleting from %s where %s;",
                table, condition), e.toString());
            throw new QueryFailedException();
        }
    }
}
