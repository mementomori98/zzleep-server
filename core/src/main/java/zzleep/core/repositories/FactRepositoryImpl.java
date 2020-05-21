package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Fact;

import java.util.List;
import java.util.Random;

@Component
public class FactRepositoryImpl implements FactRepository {

    private static final String TABLE_NAME = "datamodels.fact";
    private static final String COL_FACT_ID = "factId";
    private static final String COL_TITLE = "title";
    private static final String COL_CONTENT = "content";

    private static final Context.ResultSetExtractor<Fact> extractor = row -> new Fact(
        row.getInt(COL_FACT_ID),
        row.getString(COL_TITLE),
        row.getString(COL_CONTENT)
    );

    private final Context context;

    public FactRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Fact> getAll() {
        return context.selectAll(TABLE_NAME, extractor);
    }

    @Override
    public Fact get(int previousFactId) {
        List<Fact> available = context.select(TABLE_NAME, String.format("%s != %d", COL_FACT_ID, previousFactId), extractor);
        Random random = new Random();
        return available.get(random.nextInt(available.size()));
    }
}
