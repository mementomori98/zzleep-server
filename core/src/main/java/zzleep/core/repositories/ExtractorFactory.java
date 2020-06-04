package zzleep.core.repositories;

import zzleep.core.models.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExtractorFactory {

    private static final Context.ResultSetExtractor<Preferences> preferencesExtractor = row ->
        new Preferences(
            row.getString(DatabaseConstants.PREFERENCES_COL_DEVICE_ID),
            row.getBoolean(DatabaseConstants.PREFERENCES_COL_REGULATIONS_ENABLED),
            row.getInt(DatabaseConstants.PREFERENCES_COL_CO2_MAX),
            row.getDouble(DatabaseConstants.PREFERENCES_COL_HUMIDITY_MIN),
            row.getDouble(DatabaseConstants.PREFERENCES_COL_HUMIDITY_MAX),
            row.getInt(DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MIN),
            row.getInt(DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MAX)
        );

    private static final Context.ResultSetExtractor<Sleep> sleepExtractor = row -> new Sleep(
        row.getInt(DatabaseConstants.SLEEP_COL_SLEEP_ID),
        row.getString(DatabaseConstants.SLEEP_COL_DEVICE_ID),
        row.getObject(DatabaseConstants.SLEEP_COL_START_TIME, LocalDateTime.class),
        row.getObject(DatabaseConstants.SLEEP_COL_FINISH_TIME, LocalDateTime.class),
        row.getInt(DatabaseConstants.SLEEP_COL_RATING)
    );

    private static final Context.ResultSetExtractor<Fact> factsExtractor = row -> new Fact(
        row.getInt(DatabaseConstants.FACT_COL_FACT_ID),
        row.getString(DatabaseConstants.FACT_COL_TITLE),
        row.getString(DatabaseConstants.FACT_COL_CONTENT),
            row.getString(DatabaseConstants.FACT_COL_SOURCE_TITLE),
            row.getString(DatabaseConstants.FACT_COL_SOURCE_URL)
    );

    private static final Context.ResultSetExtractor<RoomCondition> roomConditionsExtractor = row -> {
        BigDecimal sound = row.getObject(DatabaseConstants.RC_COL_SOUND, BigDecimal.class);
        BigDecimal humidity = row.getObject(DatabaseConstants.RC_COL_HUMIDITY, BigDecimal.class);
        Double h = humidity == null ? null:humidity.doubleValue();
        Double s = sound == null ? null:sound.doubleValue();
        return new RoomCondition(
                row.getInt(DatabaseConstants.RC_COL_SLEEP_ID),
                row.getObject(DatabaseConstants.RC_COL_TIME, LocalDateTime.class),
                row.getObject(DatabaseConstants.RC_COL_TEMPERATURE, Integer.class),
                row.getObject(DatabaseConstants.RC_COL_CO2, Integer.class),
                s,
                h
        );
    };

    private static final Context.ResultSetExtractor<AuthObject> authObjectExtractor = row -> new AuthObject(
        row.getString(DatabaseConstants.AUTH_DEVICE_ID),
        row.getString(DatabaseConstants.AUTH_USER_ID),
        row.getInt(DatabaseConstants.AUTH_SLEEP_ID)
    );

    private static final Context.ResultSetExtractor<SleepSession> sleepSessionExtractor = row -> new SleepSession(
        row.getInt(DatabaseConstants.SLEEP_SESSION_COL_SLEEP_ID),
        row.getString(DatabaseConstants.SLEEP_SESSION_COL_DEVICE_ID),
        row.getObject(DatabaseConstants.SLEEP_SESSION_COL_TIME_START, LocalDateTime.class),
        row.getObject(DatabaseConstants.SLEEP_SESSION_COL_TIME_FINISH, LocalDateTime.class),
        row.getInt(DatabaseConstants.SLEEP_SESSION_COL_RATING),
        row.getDouble(DatabaseConstants.SLEEP_SESSION_COL_AVERAGE_CO2),
        row.getDouble(DatabaseConstants.SLEEP_SESSION_COL_AVERAGE_HUMIDITY),
        row.getDouble(DatabaseConstants.SLEEP_SESSION_COL_AVERAGE_SOUND),
        row.getDouble(DatabaseConstants.SLEEP_SESSION_COL_AVERAGE_TEMPERATURE)
    );
    private static final Context.ResultSetExtractor<RoomCondition> dwRoomConditionExtractor = row -> new RoomCondition(
        row.getInt(DatabaseConstants.DW_COL_SLEEP_ID),
        row.getObject(DatabaseConstants.DW_COL_TIMESTAMP, LocalDateTime.class),
        row.getInt(DatabaseConstants.DW_COL_TEMPERATURE),
        row.getInt(DatabaseConstants.DW_COL_CO2),
        row.getDouble(DatabaseConstants.DW_COL_SOUND),
        row.getDouble(DatabaseConstants.DW_COL_HUMIDITY)
    );


    private static Context.ResultSetExtractor<Device> deviceExtractor = row -> new Device(
        row.getString(DatabaseConstants.DEVICE_COL_ID),
        row.getString(DatabaseConstants.DEVICE_COL_ROOM_NAME),
        row.getString(DatabaseConstants.DEVICE_COL_USER_ID)
    );

    private static final Context.ResultSetExtractor<Integer> sleepIdExtractor = row-> row.getInt(DatabaseConstants.SLEEP_COL_SLEEP_ID);

    private static final Context.ResultSetExtractor<String> deviceIdExtractor = row-> ""+row.getString(DatabaseConstants.DEVICE_COL_ID);

    public static Context.ResultSetExtractor<SleepSession> getSleepSessionExtractor() {
        return sleepSessionExtractor;
    }

    public static Context.ResultSetExtractor<RoomCondition> getRoomConditionsExtractor() {
        return roomConditionsExtractor;
    }

    public static Context.ResultSetExtractor<Fact> getFactsExtractor() {
        return factsExtractor;
    }

    public static Context.ResultSetExtractor<Sleep> getSleepExtractor() {
        return sleepExtractor;
    }

    public static Context.ResultSetExtractor<Preferences> getPreferencesExtractor() {
        return preferencesExtractor;
    }

    public static Context.ResultSetExtractor<RoomCondition> getDWRoomConditionExtractor() {
        return dwRoomConditionExtractor;
    }

    public static Context.ResultSetExtractor<Device> getDeviceExtractor() {
        return deviceExtractor;
    }

    public static Context.ResultSetExtractor<AuthObject> getAuthObjectExtractor() {
        return authObjectExtractor;
    }

    public static Context.ResultSetExtractor<Integer> getSleepIdExtractor() {
        return sleepIdExtractor;
    }

    public static Context.ResultSetExtractor<String> getDeviceIdExtractor() {
        return deviceIdExtractor;
    }
}
