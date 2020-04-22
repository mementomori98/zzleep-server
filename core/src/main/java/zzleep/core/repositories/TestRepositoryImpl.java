package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.TestModel;

@Component
public class TestRepositoryImpl implements TestRepository {

    private final Context context;

    public TestRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public TestModel get() {
        return context.select("test", row -> new TestModel(
                row.getString("message")
        )).get(0);
    }
}
