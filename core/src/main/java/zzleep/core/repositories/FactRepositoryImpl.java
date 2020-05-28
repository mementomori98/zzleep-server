package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Fact;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Random;

@Component
public class FactRepositoryImpl implements FactRepository {

    private static final Context.ResultSetExtractor<Fact> extractor = row -> new Fact(
        row.getInt(DatabaseConstants.FACT_COL_FACT_ID),
        row.getString(DatabaseConstants.FACT_COL_TITLE),
        row.getString(DatabaseConstants.FACT_COL_CONTENT)
    );

    private final Context context;

    public FactRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Fact> getAll() {
        return context.selectAll(DatabaseConstants.FACTS_TABLE_NAME, extractor);
    }

    @Override
    public Fact get(int previousFactId) {
        List<Fact> available = context.select(
            DatabaseConstants.FACTS_TABLE_NAME,
            String.format("%s != %d", DatabaseConstants.FACT_COL_FACT_ID, previousFactId),
            extractor
        );
        Random random = new Random();
        return available.get(random.nextInt(available.size()));
    }
}
