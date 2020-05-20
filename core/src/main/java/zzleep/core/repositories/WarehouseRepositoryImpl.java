package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private static final String TABLE_NAME =
        "dw.factRoomConditions " +
            "inner join dw.dimDevice on factRoomConditions.deviceKey = dimDevice.deviceKey " +
            "inner join dw.dimRating on factRoomConditions.ratingKey = dimRating.ratingKey " +
            "inner join dw.dimSleep on factRoomConditions.sleepKey = dimSleep.sleepKey";

    private static final String COL_SLEEP_ID = "sleepId";
    private static final String COL_TIMESTAMP = "timeRecorded";
    private static final String COL_TEMPERATURE = "temperatureLevel";
    private static final String COL_HUMIDITY = "humidityLevel";
    private static final String COL_SOUND = "soundLevel";
    private static final String COL_CO2 = "co2Level";
    private static final String COL_DEVICE_ID = "deviceId";
    private static final String COL_RATING = "rating";

    private static final String COL_TIME_START = "timeStart";
    private static final String COL_TIME_FINISH = "timeFinish";
    private static final String COL_AVERAGE_CO2 = "avgCo2";
    private static final String COL_AVERAGE_HUMIDITY = "avgHumidity";
    private static final String COL_AVERAGE_TEMPERATURE = "avgTemperature";
    private static final String COL_AVERAGE_SOUND = "avgSound";

    private static final String SLEEP_SESSION_SELECTOR =
        String.format("%s, %s, min(%s) as %s, max(%s) as %s, %s, avg(%s) as %s, avg(%s) as %s, avg(%s) as %s, avg(%s) as %s",
            COL_SLEEP_ID, COL_DEVICE_ID,
            COL_TIMESTAMP, COL_TIME_START,
            COL_TIMESTAMP, COL_TIME_FINISH,
            COL_RATING,
            COL_CO2, COL_AVERAGE_CO2,
            COL_HUMIDITY, COL_AVERAGE_HUMIDITY,
            COL_SOUND, COL_AVERAGE_SOUND,
            COL_TEMPERATURE, COL_AVERAGE_TEMPERATURE
        );

    private static final String SLEEP_SESSION_GROUPER = String.format("%s, %s, %s", COL_SLEEP_ID, COL_DEVICE_ID, COL_RATING);

    private static final Context.ResultSetExtractor<RoomCondition> roomConditionExtractor = row -> new RoomCondition(
        row.getInt(COL_SLEEP_ID),
        row.getObject(COL_TIMESTAMP, LocalDateTime.class),
        row.getDouble(COL_TEMPERATURE),
        row.getDouble(COL_CO2),
        row.getDouble(COL_SOUND),
        row.getDouble(COL_HUMIDITY)
    );

    private static final Context.ResultSetExtractor<SleepSession> sleepSessionExtractor = row -> new SleepSession(
        row.getInt(COL_SLEEP_ID),
        row.getString(COL_DEVICE_ID),
        row.getObject(COL_TIME_START, LocalDateTime.class),
        row.getObject(COL_TIME_FINISH, LocalDateTime.class),
        row.getInt(COL_RATING),
        row.getDouble(COL_AVERAGE_CO2),
        row.getDouble(COL_AVERAGE_HUMIDITY),
        row.getDouble(COL_AVERAGE_SOUND),
        row.getDouble(COL_AVERAGE_TEMPERATURE)
    );

    private final Context context;

    public WarehouseRepositoryImpl(
        Context context
    ) {
        this.context = context;
    }

    @Override
    public SleepData getSleepData(int sleepId) {
        List<RoomCondition> roomConditions = context.select(
            TABLE_NAME,
            String.format("%s = %d", COL_SLEEP_ID, sleepId),
            roomConditionExtractor
        );
        if (roomConditions.isEmpty()) return null;
        List<SleepSession> query = context.selectComplex(
            TABLE_NAME, SLEEP_SESSION_SELECTOR,
            String.format("%s = %d", COL_SLEEP_ID, sleepId),
            SLEEP_SESSION_GROUPER,
            sleepSessionExtractor
        );
        if (query.isEmpty()) return null;
        SleepSession session = query.get(0);
        return new SleepData(sleepId, session.getDeviceId(), session.getTimeStart(), session.getTimeFinish(), session.getRating(), roomConditions);
    }

    @Override
    public IntervalReport getReport(String deviceId, Interval interval) {
        List<SleepSession> query = context.selectComplex(
          TABLE_NAME, SLEEP_SESSION_SELECTOR,
            String.format("%s = '%s'", COL_DEVICE_ID, deviceId),
            SLEEP_SESSION_GROUPER,
            sleepSessionExtractor
        );
        return new IntervalReport(query);
    }

    @Override
    public RoomCondition getIdealRoomCondition(String deviceId) {
        return null;
    }
}
