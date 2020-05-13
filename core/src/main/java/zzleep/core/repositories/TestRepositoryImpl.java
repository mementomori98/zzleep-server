package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.TestModel;

import java.util.List;

@Component
public class TestRepositoryImpl implements TestRepository {

    private static final String TABLE_NAME = "test";
    private static final String COL_ID = "id";
    private static final String COL_MESSAGE = "content";

    private static final Context.ResultSetExtractor<TestModel> extractor = row ->
        new TestModel(row.getInt(COL_ID), row.getString(COL_MESSAGE));

    private final PostgresContext context;

    public TestRepositoryImpl(PostgresContext context) {
        this.context = context;
    }

    @Override
    public TestModel add(TestModel model) {
        return context.insert(TABLE_NAME, COL_MESSAGE, String.format("'%s'", model.getMessage()), extractor);
    }

    @Override
    public TestModel get(int id) {
        return context.single(TABLE_NAME, String.format("%s = %d", COL_ID, id), extractor);
    }

    @Override
    public List<TestModel> getAll() {
        return context.select(TABLE_NAME, extractor);
    }

    @Override
    public TestModel update(TestModel model) {
        if (get(model.getId()) == null)
            return add(model);
        return context.update(TABLE_NAME,
            String.format("%s = '%s'", COL_MESSAGE, model.getMessage()),
            String.format("%s = %d", COL_ID, model.getId()), extractor);
    }

    @Override
    public void delete(int id) {
        context.delete(TABLE_NAME, String.format("%s = %d", COL_ID, id));
    }
}
