package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.RoomCondition;
import zzleep.core.models.Sleep;

import java.time.LocalDateTime;

@Component
public class RoomConditionsRepositoryImpl implements RoomConditionsRepository {

    private Context context;

    public RoomConditionsRepositoryImpl(Context context) {
        this.context = context;
    }

    private static final Context.ResultSetExtractor<RoomCondition> extractor = ExtractorFactory.getRoomConditionsExtractor();
    private static final Context.ResultSetExtractor<Sleep> sleepExtractor = ExtractorFactory.getSleepExtractor();

    @Override
    public RoomCondition getCurrentData(String deviceId) throws SleepNotFoundException, NoDataException {
        Sleep sleep = context.single(DatabaseConstants.SLEEP_TABLE_NAME, String.format("%s = '%s' and %s is null", DatabaseConstants.SLEEP_COL_DEVICE_ID, deviceId, DatabaseConstants.SLEEP_COL_FINISH_TIME), sleepExtractor);
        RoomCondition roomConditions;
        if(sleep == null)
        {
            throw new SleepNotFoundException();
        }
        else
        {
            roomConditions = context.single(DatabaseConstants.RC_TABLE_NAME, String.format("%s = '%s' order by %s desc limit 1", DatabaseConstants.RC_COL_SLEEP_ID, sleep.getSleepId(), DatabaseConstants.RC_COL_TIME), extractor);
            if(roomConditions == null)
                throw new NoDataException();
            else return roomConditions;
        }
    }
}
