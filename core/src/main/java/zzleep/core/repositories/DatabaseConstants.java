package zzleep.core.repositories;

public class DatabaseConstants {
    //preferences
    static final String PREFERENCES_TABLE_NAME = "datamodels.preferences";
    static final String PREFERENCES_COL_DEVICE_ID = "deviceId";
    static final String PREFERENCES_COL_REGULATIONS_ENABLED= "regulationEnabled";
    static final String PREFERENCES_COL_CO2_MAX= "co2max";
    static final String PREFERENCES_COL_HUMIDITY_MIN= "humidityMin";
    static final String PREFERENCES_COL_HUMIDITY_MAX= "humidityMax";
    static final String PREFERENCES_COL_TEMPERATURE_MIN= "temperatureMin";
    static final String PREFERENCES_COL_TEMPERATURE_MAX= "temperatureMax";

    //sleep
    static final String SLEEP_TABLE_NAME = "datamodels.sleep";
    static final String SLEEP_COL_SLEEP_ID = "sleepId";
    static final String SLEEP_COL_DEVICE_ID = "deviceId";
    static final String SLEEP_COL_START_TIME = "dateTimeStart";
    static final String SLEEP_COL_FINISH_TIME = "dateTimeEnd";
    static final String SLEEP_COL_RATING = "rating";

    //facts
    static final String FACTS_TABLE_NAME = "datamodels.fact";
    static final String FACT_COL_FACT_ID = "factId";
    static final String FACT_COL_TITLE = "title";
    static final String FACT_COL_CONTENT = "content";
    static final String FACT_COL_SOURCE_TITLE = "sourceTitle";
    static final String FACT_COL_SOURCE_URL = "sourceUrl";

    //room conditions
    static final String RC_TABLE_NAME = "datamodels.roomConditions";
    static final String RC_COL_SLEEP_ID = "sleepId";
    static final String RC_COL_TIME = "time";
    static final String RC_COL_TEMPERATURE = "temperature";
    static final String RC_COL_CO2 = "co2";
    static final String RC_COL_SOUND = "sound";
    static final String RC_COL_HUMIDITY = "humidity";

    //sleep sessions
    static final String SLEEP_SESSION_COL_SLEEP_ID = "sleepId";
    static final String SLEEP_SESSION_COL_DEVICE_ID = "deviceId";
    static final String SLEEP_SESSION_COL_RATING = "rating";
    static final String SLEEP_SESSION_COL_TIME_START = "timeStart";
    static final String SLEEP_SESSION_COL_TIME_FINISH = "timeFinish";
    static final String SLEEP_SESSION_COL_AVERAGE_CO2 = "avgCo2";
    static final String SLEEP_SESSION_COL_AVERAGE_HUMIDITY = "avgHumidity";
    static final String SLEEP_SESSION_COL_AVERAGE_TEMPERATURE = "avgTemperature";
    static final String SLEEP_SESSION_COL_AVERAGE_SOUND = "avgSound";


    //device
    static final String DEVICE_TABLE_NAME = "datamodels.device";
    static final String DEVICE_COL_ID = "deviceId";
    static final String DEVICE_COL_USER_ID = "userId";
    static final String DEVICE_COL_ROOM_NAME = "roomName";

    //dw
    static final String DW_TABLE_NAME =
            "dw.factRoomConditions " +
                    "inner join dw.dimDevice on factRoomConditions.deviceKey = dimDevice.deviceKey " +
                    "left outer join dw.dimRating on factRoomConditions.ratingKey = dimRating.ratingKey " +
                    "inner join dw.dimSleep on factRoomConditions.sleepKey = dimSleep.sleepKey";
    static final String DW_COL_SLEEP_ID = "sleepId";
    static final String DW_COL_TIMESTAMP = "timeRecorded";
    static final String DW_COL_TEMPERATURE = "temperatureLevel";
    static final String DW_COL_HUMIDITY = "humidityLevel";
    static final String DW_COL_SOUND = "soundLevel";
    static final String DW_COL_CO2 = "co2Level";
    static final String DW_COL_DEVICE_ID = "deviceId";
    static final String DW_COL_RATING = "rating";
    static final String DW_COL_TIME_START = "timeStart";
    static final String DW_COL_TIME_FINISH = "timeFinish";
    static final String DW_COL_AVERAGE_CO2 = "avgCo2";
    static final String DW_COL_AVERAGE_HUMIDITY = "avgHumidity";
    static final String DW_COL_AVERAGE_TEMPERATURE = "avgTemperature";
    static final String DW_COL_AVERAGE_SOUND = "avgSound";

    // auth
    static final String AUTH_TABLE_NAME =
        "datamodels.device " +
            "left outer join datamodels.sleep on device.deviceId = sleep.deviceId ";
    static final String AUTH_SELECTOR = "device.userId, device.deviceId, sleep.sleepId";
    static final String AUTH_USER_ID = "userId";
    static final String AUTH_DEVICE_ID = "deviceId";
    static final String AUTH_SLEEP_ID = "sleepId";

}
