package zzleep.core.repositories;

import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import zzleep.core.models.Sleep;
import zzleep.core.models.TestModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SleepRepositoryImpl implements SleepRepository {
    private static final String TABLE_NAME = "datamodels.sleep";
    private static final String COL_SLEEP_ID = "sleepId";
    private static final String COL_DEVICE_ID = "deviceId";
    private static final String COL_START_TIME = "dateTimeStart";
    private static final String COL_FINISH_TIME = "dateTimeEnd";
    private static final String COL_RATING = "rating";

    private Context context;

    private static final Context.ResultSetExtractor<Sleep> extractor = row ->
            new Sleep(row.getInt(COL_SLEEP_ID), row.getString(COL_DEVICE_ID), row.getObject(COL_START_TIME, LocalDateTime.class), row.getObject(COL_FINISH_TIME, LocalDateTime.class), row.getInt(COL_RATING));

    public SleepRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Sleep startTracking(String deviceId) throws SleepNotStoppedException {
        Sleep sleep = context.single(TABLE_NAME, String.format("%s = '%s' and %s is null", COL_DEVICE_ID, deviceId, COL_FINISH_TIME), extractor);
        if(sleep != null)
        {
            throw new SleepNotStoppedException();
        }
        else
            sleep = context.insert(TABLE_NAME, String.format("%s, %s", COL_DEVICE_ID, COL_START_TIME), String.format("'%s', '%s'", deviceId, dateToString(LocalDateTime.now())), extractor);
        return sleep;
    }

    @Override
    public Sleep stopTracking(String deviceId) throws SleepNotStartedException {
        Sleep sleep = context.single(TABLE_NAME, String.format("%s = '%s' and %s is null", COL_DEVICE_ID, deviceId, COL_FINISH_TIME), extractor);
        if(sleep == null)
        {
            throw new SleepNotStartedException();
        }
        else sleep = context.update(TABLE_NAME, String.format("%s = '%s'", COL_FINISH_TIME, dateToString(LocalDateTime.now())), String.format("%s = '%s'", COL_SLEEP_ID, sleep.getSleepId()), extractor);
        return sleep;
    }

    @Override
    public Sleep rateSleep(String sleepId, int rating) throws SleepNotFoundException {
        Sleep sleep = context.single(TABLE_NAME, String.format("%s = '%s'", COL_SLEEP_ID, sleepId), extractor);
        if(sleep == null)
        {
            throw new SleepNotFoundException();
        }
        else sleep = context.update(TABLE_NAME, String.format("%s = '%d'", COL_RATING, rating), String.format("%s = '%s'", COL_SLEEP_ID, sleep.getSleepId()), extractor);
        return sleep;
    }

    private String dateToString(LocalDateTime date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }
}
