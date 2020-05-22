package zzleep.core.repositories;

import zzleep.core.models.*;

import java.time.LocalDateTime;

public class ExtractorFactory {

    //preferences
    private static final String PREFERENCES_COL_DEVICE_ID = "deviceId";
    private static final String PREFERENCES_COL_REGULATIONS_ENABLED= "regulationEnabled";
    private static final String PREFERENCES_COL_CO2_MAX= "co2max";
    private static final String PREFERENCES_COL_HUMIDITY_MIN= "humidityMin";
    private static final String PREFERENCES_COL_HUMIDITY_MAX= "humidityMax";
    private static final String PREFERENCES_COL_TEMPERATURE_MIN= "temperatureMin";
    private static final String PREFERENCES_COL_TEMPERATURE_MAX= "temperatureMax";

    private static final Context.ResultSetExtractor<Preferences> preferencesExtractor = row ->
            new Preferences(row.getString(PREFERENCES_COL_DEVICE_ID), row.getBoolean(PREFERENCES_COL_REGULATIONS_ENABLED), row.getInt(PREFERENCES_COL_CO2_MAX), row.getDouble(PREFERENCES_COL_HUMIDITY_MIN), row.getDouble(PREFERENCES_COL_HUMIDITY_MAX), row.getInt(PREFERENCES_COL_TEMPERATURE_MIN), row.getInt(PREFERENCES_COL_TEMPERATURE_MAX));

    public static Context.ResultSetExtractor<Preferences> getPreferencesExtractor() {
        return preferencesExtractor;
    }

    //sleep
    private static final String SLEEP_COL_SLEEP_ID = "sleepId";
    private static final String SLEEP_COL_DEVICE_ID = "deviceId";
    private static final String SLEEP_COL_START_TIME = "dateTimeStart";
    private static final String SLEEP_COL_FINISH_TIME = "dateTimeEnd";
    private static final String SLEEP_COL_RATING = "rating";

    private static final Context.ResultSetExtractor<Sleep> sleepExtractor = row ->
            new Sleep(row.getInt(SLEEP_COL_SLEEP_ID), row.getString(SLEEP_COL_DEVICE_ID), row.getObject(SLEEP_COL_START_TIME, LocalDateTime.class), row.getObject(SLEEP_COL_FINISH_TIME, LocalDateTime.class), row.getInt(SLEEP_COL_RATING));

    public static Context.ResultSetExtractor<Sleep> getSleepExtractor() {
        return sleepExtractor;
    }

    //facts
    private static final String FACT_COL_FACT_ID = "factId";
    private static final String FACT_COL_TITLE = "title";
    private static final String FACT_COL_CONTENT = "content";

    private static final Context.ResultSetExtractor<Fact> factsExtractor = row -> new Fact(
            row.getInt(FACT_COL_FACT_ID),
            row.getString(FACT_COL_TITLE),
            row.getString(FACT_COL_CONTENT)
    );

    public static Context.ResultSetExtractor<Fact> getFactsExtractor() {
        return factsExtractor;
    }

    //room conditions
    private static final String RC_COL_SLEEP_ID = "sleepId";
    private static final String RC_COL_TIME = "time";
    private static final String RC_COL_TEMPERATURE = "temperature";
    private static final String RC_COL_CO2 = "co2";
    private static final String RC_COL_SOUND = "sound";
    private static final String RC_COL_HUMIDITY = "humidity";

    private static final Context.ResultSetExtractor<RoomCondition> roomConditionsExtractor = row ->
            new RoomCondition(row.getInt(RC_COL_SLEEP_ID), row.getObject(RC_COL_TIME, LocalDateTime.class), row.getInt(RC_COL_TEMPERATURE), row.getInt(RC_COL_CO2), row.getDouble(RC_COL_SOUND), row.getDouble(RC_COL_HUMIDITY));

    public static Context.ResultSetExtractor<RoomCondition> getRoomConditionsExtractor() {
        return roomConditionsExtractor;
    }

    //sleep sessions

    private static final String SLEEP_SESSION_COL_SLEEP_ID = "sleepId";
    private static final String SLEEP_SESSION_COL_DEVICE_ID = "deviceId";
    private static final String SLEEP_SESSION_COL_RATING = "rating";
    private static final String SLEEP_SESSION_COL_TIME_START = "timeStart";
    private static final String SLEEP_SESSION_COL_TIME_FINISH = "timeFinish";
    private static final String SLEEP_SESSION_COL_AVERAGE_CO2 = "avgCo2";
    private static final String SLEEP_SESSION_COL_AVERAGE_HUMIDITY = "avgHumidity";
    private static final String SLEEP_SESSION_COL_AVERAGE_TEMPERATURE = "avgTemperature";
    private static final String SLEEP_SESSION_COL_AVERAGE_SOUND = "avgSound";

    private static final Context.ResultSetExtractor<SleepSession> sleepSessionExtractor = row -> new SleepSession(
            row.getInt(SLEEP_SESSION_COL_SLEEP_ID),
            row.getString(SLEEP_SESSION_COL_DEVICE_ID),
            row.getObject(SLEEP_SESSION_COL_TIME_START, LocalDateTime.class),
            row.getObject(SLEEP_SESSION_COL_TIME_FINISH, LocalDateTime.class),
            row.getInt(SLEEP_SESSION_COL_RATING),
            row.getDouble(SLEEP_SESSION_COL_AVERAGE_CO2),
            row.getDouble(SLEEP_SESSION_COL_AVERAGE_HUMIDITY),
            row.getDouble(SLEEP_SESSION_COL_AVERAGE_SOUND),
            row.getDouble(SLEEP_SESSION_COL_AVERAGE_TEMPERATURE)
    );

    public static Context.ResultSetExtractor<SleepSession> getSleepSessionExtractor() {
        return sleepSessionExtractor;
    }

    //dw room conditions
    private static final String COL_SLEEP_ID = "sleepId";
    private static final String COL_TIMESTAMP = "timeRecorded";
    private static final String COL_TEMPERATURE = "temperatureLevel";
    private static final String COL_HUMIDITY = "humidityLevel";
    private static final String COL_SOUND = "soundLevel";
    private static final String COL_CO2 = "co2Level";

    private static final Context.ResultSetExtractor<RoomCondition> dwRoomConditionExtractor = row -> new RoomCondition(
            row.getInt(COL_SLEEP_ID),
            row.getObject(COL_TIMESTAMP, LocalDateTime.class),
            row.getInt(COL_TEMPERATURE),
            row.getInt(COL_CO2),
            row.getDouble(COL_SOUND),
            row.getDouble(COL_HUMIDITY)
    );

    public static Context.ResultSetExtractor<RoomCondition> getDWRoomConditionExtractor() {
        return dwRoomConditionExtractor;
    }
}
