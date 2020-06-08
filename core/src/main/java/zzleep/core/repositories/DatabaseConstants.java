package zzleep.core.repositories;

public class DatabaseConstants {
    //preferences
    public static final String PREFERENCES_TABLE_NAME = "datamodels.preferences";
    public static final String PREFERENCES_COL_DEVICE_ID = "deviceId";
    public static final String PREFERENCES_COL_REGULATIONS_ENABLED= "regulationEnabled";
    public static final String PREFERENCES_COL_CO2_MAX= "co2max";
    public static final String PREFERENCES_COL_HUMIDITY_MIN= "humidityMin";
    public static final String PREFERENCES_COL_HUMIDITY_MAX= "humidityMax";
    public static final String PREFERENCES_COL_TEMPERATURE_MIN= "temperatureMin";
    public static final String PREFERENCES_COL_TEMPERATURE_MAX= "temperatureMax";

    //sleep
    public static final String SLEEP_TABLE_NAME = "datamodels.sleep";
    public static final String SLEEP_COL_SLEEP_ID = "sleepId";
    public static final String SLEEP_COL_DEVICE_ID = "deviceId";
    public static final String SLEEP_COL_START_TIME = "dateTimeStart";
    public static final String SLEEP_COL_FINISH_TIME = "dateTimeEnd";
    public static final String SLEEP_COL_RATING = "rating";

    //facts
    public static final String FACTS_TABLE_NAME = "datamodels.fact";
    public static final String FACT_COL_FACT_ID = "factId";
    public static final String FACT_COL_TITLE = "title";
    public static final String FACT_COL_CONTENT = "content";
    public static final String FACT_COL_SOURCE_TITLE = "sourceTitle";
    public static final String FACT_COL_SOURCE_URL = "sourceUrl";

    //room conditions
    public static final String RC_TABLE_NAME = "datamodels.roomConditions";
    public static final String RC_COL_SLEEP_ID = "sleepId";
    public static final String RC_COL_TIME = "time";
    public static final String RC_COL_TEMPERATURE = "temperature";
    public static final String RC_COL_CO2 = "co2";
    public static final String RC_COL_SOUND = "sound";
    public static final String RC_COL_HUMIDITY = "humidity";

    //sleep sessions
    static final String SLEEP_SESSION_COL_SLEEP_ID = "sleepId";
    static final String SLEEP_SESSION_COL_DEVICE_ID = "deviceId";
    static final String SLEEP_SESSION_COL_RATING = "rating";
    static final String SLEEP_SESSION_COL_TIME_START = "dateTimeStart";
    static final String SLEEP_SESSION_COL_TIME_FINISH = "dateTimeEnd";
    static final String SLEEP_SESSION_COL_AVERAGE_CO2 = "avgCo2";
    static final String SLEEP_SESSION_COL_AVERAGE_HUMIDITY = "avgHumidity";
    static final String SLEEP_SESSION_COL_AVERAGE_TEMPERATURE = "avgTemperature";
    static final String SLEEP_SESSION_COL_AVERAGE_SOUND = "avgSound";


    //device
    public static final String DEVICE_TABLE_NAME = "datamodels.device";
    public static final String DEVICE_COL_ID = "deviceId";
    public static final String DEVICE_COL_USER_ID = "userId";
    public static final String DEVICE_COL_ROOM_NAME = "roomName";

    //dw
    public static final String DW_TABLE_NAME =
            "dw.factRoomConditions " +
                    "inner join dw.dimDevice on factRoomConditions.deviceKey = dimDevice.deviceKey " +
                    "inner join dw.dimSleep on factRoomConditions.sleepKey = dimSleep.sleepKey";

    static final String DW_COL_SLEEP_ID = "sleepId";
    static final String DW_COL_TIMESTAMP = "timeRecorded";
    static final String DW_COL_TEMPERATURE = "temperatureLevel";
    static final String DW_COL_HUMIDITY = "humidityLevel";
    static final String DW_COL_SOUND = "soundLevel";
    static final String DW_COL_CO2 = "co2Level";
    static final String DW_COL_DEVICE_ID = "deviceId";
    static final String DW_COL_RATING = "rating";
    static final String DW_COL_TIME_START = "dateTimeStart";
    static final String DW_COL_TIME_FINISH = "dateTimeEnd";
    static final String DW_COL_AVERAGE_CO2 = "avgCo2";
    static final String DW_COL_AVERAGE_HUMIDITY = "avgHumidity";
    static final String DW_COL_AVERAGE_TEMPERATURE = "avgTemperature";
    static final String DW_COL_AVERAGE_SOUND = "avgSound";

    // auth
    public static final String AUTH_TABLE_NAME =
        "datamodels.device " +
            "left outer join datamodels.sleep on device.deviceId = sleep.deviceId ";
    public static final String AUTH_SELECTOR = "device.userId, device.deviceId, sleep.sleepId";
    public static final String AUTH_USER_ID = "userId";
    public static final String AUTH_DEVICE_ID = "deviceId";
    public static final String AUTH_SLEEP_ID = "sleepId";

    //active sleeps
    public static final String ACTIVE_SLEEPS_TABLE = "datamodels.activesleeps";
    public static final String ACTIVE_SLEEPS_COL_SLEEP_ID = "sleepId";

    //active ventilation
    public static final String ACTIVE_VENTILATION_TABLE = "datamodels.activeventilation";
    public static final String ACTIVE_VENTILATION_COL_DEVICE_ID = "deviceid";
}
