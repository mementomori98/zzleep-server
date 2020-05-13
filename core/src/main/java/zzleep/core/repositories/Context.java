package zzleep.core.repositories;

import java.sql.ResultSet;
import java.util.List;

public interface Context {

    /**
     * Example: context.insert("User", "name, email, age", "'John', 'john@example.com', 23");
     * @param table the name of the table you'd like to insert into (e.g. "RoomCondition")
     * @param columns comma-separated list of column names (e.g. "name, email")
     * @param values sql-specific list of values (e.g. "'John', 'Deer', 32") - note the single quotation on strings)
     */
    <TType> TType insert(
        String table,
        String columns,
        String values,
        ResultSetExtractor<TType> extractor
    );

    /**
     * Select a list of items that meet the specified condition
     * @param table the name of the table including the schema (e.g. "datamodels.User")
     * @param condition the condition to meet (after the where keyword, e.g. "name like '%John%'")
     * @param extractor the lambda expression used to extract a generic type from the ResultSet (e.g. row -> new User(row.getString("name"), row.getString("email")
     * @param <TType> the type returned by the extractor
     * @return the list of items returned by the query
     */
    <TType> List<TType> select(
        String table,
        String condition,
        ResultSetExtractor<TType> extractor
    );

    /**
     * Select all items from a table
     * @param table the name of the table including the schema (e.g. "datamodels.User")
     * @param extractor the lambda expression used to extract a generic type from the ResultSet (e.g. row -> new User(row.getString("name"), row.getString("email")
     * @param <TType> the type returned by the extractor
     * @return the list of items returned by the query
     */
    <TType> List<TType> selectAll(
        String table,
        ResultSetExtractor<TType> extractor
    );

    /**
     * Select a single item from a table that meets the specified condition, returns null if not found
     * @param table the name of the table including the schema (e.g. "datamodels.User")
     * @param condition the condition to meet (after the where keyword, e.g. "name = 'John Deer'")
     * @param extractor the lambda expression used to extract a generic type from the ResultSet (e.g. row -> new User(row.getString("name"), row.getString("email")
     * @param <TType> the type returned by the extractor
     * @return the single item selected from the list or null if not found, throws 'QueryContainsMultipleElementsException' if multiple items are returned (use conditions that are met 0 or 1 time)
     */
    <TType> TType single(
        String table,
        String condition,
        ResultSetExtractor<TType> extractor
    );

    /**
     * @param table the name of the table including the schema (e.g. "datamodels.User")
     * @param fieldsWithValues the fields to update (after the set keyword, e.g. "name = 'John', age = 32")
     * @param condition the condition to meet (after the where keyword, e.g. "name = 'Smith'")
     */
    <TType> TType update(
        String table,
        String fieldsWithValues,
        String condition,
        ResultSetExtractor<TType> extractor
    );

    /**
     * Be careful, this can be used to wipe the whole table if condition is set to e.g. "true"
     * @param table the name of the table including schema (e.g. "datamodels.User")
     * @param condition the condition to meet (after the where keyword, e.g. "name = 'Smith'")
     */
    void delete(
        String table,
        String condition
    );

    /**
     * Intended to be used as a lambda expression to simplify constructing a data model from a ResultSet
     * E.g. row -> new User(
     *     row.getString("name"),
     *     row.getString("email"),
     *     row.getInt("age")
     * )
     * @param <TType> the return-type of the lambda, implied
     */
    interface ResultSetExtractor<TType> {
        TType extract(ResultSet resultSet) throws Exception;
    }

    class QueryFailedException extends RuntimeException {
    }

    class QueryEmptyException extends RuntimeException {
    }

    class QueryContainsMultipleElementsException extends RuntimeException {
    }
}
