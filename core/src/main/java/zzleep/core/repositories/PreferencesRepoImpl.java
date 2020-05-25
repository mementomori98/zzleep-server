package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Preferences;
import zzleep.core.models.Sleep;

import java.time.LocalDateTime;

@Component
public class PreferencesRepoImpl implements PreferencesRepository {
    private static final String TABLE_NAME = "datamodels.preferences";
    private static final String COL_DEVICE_ID = "deviceId";
    private static final String COL_REGULATIONS_ENABLED= "regulationEnabled";
    private static final String COL_CO2_MAX= "co2max";
    private static final String COL_HUMIDITY_MIN= "humidityMin";
    private static final String COL_HUMIDITY_MAX= "humidityMax";
    private static final String COL_TEMPERATURE_MIN= "temperatureMin";
    private static final String COL_TEMPERATURE_MAX= "temperatureMax";

    private Context context;

    private Context.ResultSetExtractor<Preferences> preferencesExtractor = ExtractorFactory.getPreferencesExtractor();

    public PreferencesRepoImpl(Context context) {
        this.context = context;
    }

    @Override
    public Preferences setPreferences(Preferences preferences) {
        //checks if device exists
        //if not, throw exception + return null
        //if yes:
        Preferences pref = context.single(TABLE_NAME, String.format("%s = '%s'", COL_DEVICE_ID, preferences.getDeviceId()), preferencesExtractor);
        if(pref == null)
        {
            context.insert(TABLE_NAME,
                    String.format("%s, %s, %s, %s, %s, %s, %s",
                            COL_DEVICE_ID,
                            COL_REGULATIONS_ENABLED,
                            COL_CO2_MAX,
                            COL_HUMIDITY_MIN,
                            COL_HUMIDITY_MAX,
                            COL_TEMPERATURE_MIN,
                            COL_TEMPERATURE_MAX),
                    String.format( "'%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                            preferences.getDeviceId(),
                            preferences.isRegulationEnabled(),
                            preferences.getCo2Max(),
                            preferences.getHumidityMin(),
                            preferences.getHumidityMax(),
                            preferences.getTemperatureMin(),
                            preferences.getTemperatureMax()
                    ), preferencesExtractor);
            return preferences;
        }
        else {
            context.update(TABLE_NAME,
                    String.format("%s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s'",
                            COL_REGULATIONS_ENABLED,
                            preferences.isRegulationEnabled(),
                            COL_CO2_MAX,
                            preferences.getCo2Max(),
                            COL_HUMIDITY_MIN,
                            preferences.getHumidityMin(),
                            COL_HUMIDITY_MAX,
                            preferences.getHumidityMax(),
                            COL_TEMPERATURE_MIN,
                            preferences.getTemperatureMin(),
                            COL_TEMPERATURE_MAX,
                            preferences.getTemperatureMax()),
                    String.format("%s = '%s'", COL_DEVICE_ID, preferences.getDeviceId()),
                    preferencesExtractor);
            return preferences;
        }
    }

    @Override
    public Preferences getPreferences(String deviceId) {
        return context.single(TABLE_NAME, String.format("%s = '%s'", COL_DEVICE_ID, deviceId), preferencesExtractor);
    }
}
