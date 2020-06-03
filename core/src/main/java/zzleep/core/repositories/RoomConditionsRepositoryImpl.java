package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.RoomCondition;
import zzleep.core.models.Sleep;

@Component
public class RoomConditionsRepositoryImpl implements RoomConditionsRepository {

    private Context context;

    private final SleepRepository sleepRepository;

    public RoomConditionsRepositoryImpl(Context context, SleepRepository sleepRepository) {
        this.context = context;
        this.sleepRepository = sleepRepository;
    }

    private static final Context.ResultSetExtractor<RoomCondition> extractor = ExtractorFactory.getRoomConditionsExtractor();
    private static final Context.ResultSetExtractor<Sleep> sleepExtractor = ExtractorFactory.getSleepExtractor();

    @Override
    public RoomCondition getCurrent(String deviceId) {
        Sleep sleep = sleepRepository.getActiveSleep(deviceId);
        if (sleep == null) throw new SleepNotFoundException();
        RoomCondition roomConditions = getLatestRoomCondition(sleep.getSleepId());
        if (roomConditions == null) throw new NoDataException();
        return roomConditions;
    }

    @Override
    public RoomCondition getLatest(String deviceId) {
        Sleep sleep = sleepRepository.getLatestSleep(deviceId);
        if (sleep == null) throw new SleepNotFoundException();
        RoomCondition roomCondition = getLatestRoomCondition(sleep.getSleepId());
        if (roomCondition == null) throw new NoDataException();
        return roomCondition;
    }

    private RoomCondition getLatestRoomCondition(int sleepId) {
        return context.single(
            DatabaseConstants.RC_TABLE_NAME,
            String.format("%s = %d order by %s desc limit 1",
                DatabaseConstants.RC_COL_SLEEP_ID, sleepId,
                DatabaseConstants.RC_COL_TIME),
            extractor
        );
    }
}
