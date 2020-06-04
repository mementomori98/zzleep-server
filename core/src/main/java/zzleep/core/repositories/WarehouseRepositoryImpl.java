package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.*;

import javax.xml.crypto.Data;
import java.util.List;

@Component
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private static final String SLEEP_SESSION_SELECTOR =
        String.format("%s, %s, min(%s) as %s, max(%s) as %s, %s, avg(%s) as %s, avg(%s) as %s, avg(%s) as %s, avg(%s) as %s",
            DatabaseConstants.DW_COL_SLEEP_ID, DatabaseConstants.DW_COL_DEVICE_ID,
            DatabaseConstants.DW_COL_TIMESTAMP, DatabaseConstants.DW_COL_TIME_START,
            DatabaseConstants.DW_COL_TIMESTAMP, DatabaseConstants.DW_COL_TIME_FINISH,
            DatabaseConstants.DW_COL_RATING,
            DatabaseConstants.DW_COL_CO2, DatabaseConstants.DW_COL_AVERAGE_CO2,
            DatabaseConstants.DW_COL_HUMIDITY, DatabaseConstants.DW_COL_AVERAGE_HUMIDITY,
            DatabaseConstants.DW_COL_SOUND, DatabaseConstants.DW_COL_AVERAGE_SOUND,
            DatabaseConstants.DW_COL_TEMPERATURE, DatabaseConstants.DW_COL_AVERAGE_TEMPERATURE
        );

    private static final String IDEAL_ROOM_CONDITION_SELECTOR =
        String.format(
            "-1 as %s, timestamp '%s' as %s, " +
                "trunc(cast(sum(%s * %s) as decimal) / sum(%s), 2) as %s, " +
                "trunc(cast(sum(%s * %s) as decimal) / sum(%s), 2) as %s, " +
                "trunc(cast(sum(%s * %s) as decimal) / sum(%s), 2) as %s, " +
                "trunc(cast(sum(%s * %s) as decimal) / sum(%s), 2) as %s",
            DatabaseConstants.DW_COL_SLEEP_ID, "1970-01-01 00:00:00.000000", DatabaseConstants.DW_COL_TIMESTAMP,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_CO2,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_CO2,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_HUMIDITY,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_HUMIDITY,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_SOUND,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_SOUND,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_TEMPERATURE,
            DatabaseConstants.DW_COL_RATING, DatabaseConstants.DW_COL_TEMPERATURE);

    private static final String SLEEP_SESSION_GROUPER = String.format(
        "%s, %s, %s", DatabaseConstants.DW_COL_SLEEP_ID, DatabaseConstants.DW_COL_DEVICE_ID, DatabaseConstants.DW_COL_RATING);

    private static final Context.ResultSetExtractor<RoomCondition> roomConditionExtractor = ExtractorFactory.getDWRoomConditionExtractor();

    private static final Context.ResultSetExtractor<SleepSession> sleepSessionExtractor = ExtractorFactory.getSleepSessionExtractor();

    private final Context context;

    public WarehouseRepositoryImpl(
        Context context
    ) {
        this.context = context;
    }

    @Override
    public SleepData getSleepData(int sleepId) {
        List<RoomCondition> roomConditions = getRoomConditions(sleepId);
        if (roomConditions.isEmpty()) return null;
        SleepSession session = getSleepSession(sleepId);
        if (session == null) return null;
        return new SleepData(
            sleepId,
            session.getDeviceId(),
            session.getTimeStart(),
            session.getTimeFinish(),
            session.getRating(),
            roomConditions
        );
    }

    @Override
    public IntervalReport getReport(String deviceId, Interval interval) {
        List<SleepSession> query = context.selectComplex(
            DatabaseConstants.DW_TABLE_NAME, SLEEP_SESSION_SELECTOR,
            String.format("%s = '%s' and %s >= '%s' and %s <= '%s'",
                    DatabaseConstants.DW_COL_DEVICE_ID, deviceId,
                    DatabaseConstants.DW_COL_TIME_START, interval.getStart(),
                    DatabaseConstants.DW_COL_TIME_FINISH, interval.getEnd()),
            SLEEP_SESSION_GROUPER,
            sleepSessionExtractor
        );
        return new IntervalReport(query);
    }

    @Override
    public IdealRoomConditions getIdealRoomCondition(String deviceId) {
        List<RoomCondition> query = getRoomConditions(deviceId);

        if (query.size() != 0) {
            RoomCondition ideal = query.get(0);
            return new IdealRoomConditions(
                ideal.getCo2(),
                ideal.getSound(),
                ideal.getHumidity(),
                ideal.getTemperature()
            );
        }

        return new IdealRoomConditions(
            21,
            600,
            50,
            50
        );
    }

    private SleepSession getSleepSession(int sleepId) {
        List<SleepSession> sleepSessions = context.selectComplex(
            DatabaseConstants.DW_TABLE_NAME, SLEEP_SESSION_SELECTOR,
            String.format("%s = %d", DatabaseConstants.DW_COL_SLEEP_ID, sleepId),
            SLEEP_SESSION_GROUPER,
            sleepSessionExtractor
        );
        return sleepSessions.isEmpty() ? null : sleepSessions.get(0);
    }

    private List<RoomCondition> getRoomConditions(int sleepId) {
        return context.select(
            DatabaseConstants.DW_TABLE_NAME,
            String.format("%s = %d", DatabaseConstants.DW_COL_SLEEP_ID, sleepId),
            roomConditionExtractor
        );
    }

    private List<RoomCondition> getRoomConditions(String deviceId) {
        return context.selectComplex(
            DatabaseConstants.DW_TABLE_NAME, IDEAL_ROOM_CONDITION_SELECTOR,
            String.format("%s = '%s'", DatabaseConstants.DW_COL_DEVICE_ID, deviceId),
            null,
            roomConditionExtractor
        );
    }
}
