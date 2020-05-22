package zzleep.core.repositories;

import zzleep.core.models.RoomCondition;
import zzleep.core.models.Sleep;

import java.time.LocalDateTime;

public class RoomConditionsRepositoryImpl implements RoomConditionsRepository {
    private static final String TABLE_NAME = "datamodels.roomConditions";
    private static final String COL_SLEEP_ID = "sleepId";
    private static final String COL_TIME = "time";
    private static final String COL_TEMPERATURE = "temperature";
    private static final String COL_CO2 = "co2";
    private static final String COL_SOUND = "sound";
    private static final String COL_HUMIDITY = "humidity";



    private static final String SLEEP_TABLE_NAME = "datamodels.sleep";
    private static final String SLEEP_COL_DEVICE_ID = "deviceId";
    private static final String SLEEP_COL_FINISH_TIME = "dateTimeEnd";
    private static final String SLEEP_COL_SLEEP_ID = "sleepId";
    private static final String SLEEP_COL_START_TIME = "dateTimeStart";
    private static final String SLEEP_COL_RATING = "rating";


    private Context context;

    private static final Context.ResultSetExtractor<RoomCondition> extractor = row ->
            new RoomCondition(row.getInt(COL_SLEEP_ID), row.getObject(COL_TIME, LocalDateTime.class), row.getInt(COL_TEMPERATURE), row.getInt(COL_CO2), row.getDouble(COL_SOUND), row.getDouble(COL_HUMIDITY));

    private static final Context.ResultSetExtractor<Sleep> sleepExtractor = row ->
            new Sleep(row.getInt(COL_SLEEP_ID), row.getString(SLEEP_COL_DEVICE_ID), row.getObject(SLEEP_COL_START_TIME, LocalDateTime.class), row.getObject(SLEEP_COL_FINISH_TIME, LocalDateTime.class), row.getInt(SLEEP_COL_RATING));

    @Override
    public RoomCondition getCurrentData(String deviceId) throws SleepNotFoundException, NoDataException {
        Sleep sleep = context.single(SLEEP_TABLE_NAME, String.format("%s = '%s' and %s is null", SLEEP_COL_DEVICE_ID, deviceId, SLEEP_COL_FINISH_TIME), sleepExtractor);
        RoomCondition roomConditions;
        if(sleep == null)
        {
            throw new SleepNotFoundException();
        }
        else
        {
            roomConditions = context.single(TABLE_NAME, String.format("%s = '%s' and order by %s desc limit 1", COL_SLEEP_ID, sleep.getSleepId(), COL_TIME), extractor);
            if(roomConditions == null)
                throw new NoDataException();
            else return roomConditions;
        }
    }
}
