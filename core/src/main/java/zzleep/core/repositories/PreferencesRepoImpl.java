package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Device;
import zzleep.core.models.Preferences;
import zzleep.core.models.Sleep;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

@Component
public class PreferencesRepoImpl implements PreferencesRepository {
    private Context context;

    private Context.ResultSetExtractor<Preferences> preferencesExtractor = ExtractorFactory.getPreferencesExtractor();
    private Context.ResultSetExtractor<Device> deviceExtractor = ExtractorFactory.getDeviceExtractor();

    public PreferencesRepoImpl(Context context) {
        this.context = context;
    }

    @Override
    public Preferences setPreferences(Preferences preferences) throws InvalidValuesException {
        if(preferences.getTemperatureMin() > preferences.getTemperatureMax() || preferences.getHumidityMin() > preferences.getHumidityMax())
            throw new InvalidValuesException();
        Device device = context.single(DatabaseConstants.DEVICE_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.DEVICE_COL_ID, preferences.getDeviceId()), deviceExtractor);
        if(device == null)
            return null;
        Preferences pref = context.single(DatabaseConstants.PREFERENCES_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.PREFERENCES_COL_DEVICE_ID, preferences.getDeviceId()), preferencesExtractor);
        if(pref == null)
        {
            context.insert(DatabaseConstants.PREFERENCES_TABLE_NAME,
                    String.format("%s, %s, %s, %s, %s, %s, %s",
                            DatabaseConstants.PREFERENCES_COL_DEVICE_ID,
                            DatabaseConstants.PREFERENCES_COL_REGULATIONS_ENABLED,
                            DatabaseConstants.PREFERENCES_COL_CO2_MAX,
                            DatabaseConstants.PREFERENCES_COL_HUMIDITY_MIN,
                            DatabaseConstants.PREFERENCES_COL_HUMIDITY_MAX,
                            DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MIN,
                            DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MAX),
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
            context.update(DatabaseConstants.PREFERENCES_TABLE_NAME,
                    String.format("%s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s'",
                            DatabaseConstants.PREFERENCES_COL_REGULATIONS_ENABLED,
                            preferences.isRegulationEnabled(),
                            DatabaseConstants.PREFERENCES_COL_CO2_MAX,
                            preferences.getCo2Max(),
                            DatabaseConstants.PREFERENCES_COL_HUMIDITY_MIN,
                            preferences.getHumidityMin(),
                            DatabaseConstants.PREFERENCES_COL_HUMIDITY_MAX,
                            preferences.getHumidityMax(),
                            DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MIN,
                            preferences.getTemperatureMin(),
                            DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MAX,
                            preferences.getTemperatureMax()),
                    String.format("%s = '%s'", DatabaseConstants.PREFERENCES_COL_DEVICE_ID, preferences.getDeviceId()),
                    preferencesExtractor);
            return preferences;
        }
    }

    @Override
    public Preferences getPreferences(String deviceId) {
        return context.single(DatabaseConstants.PREFERENCES_TABLE_NAME, String.format("%s = '%s'", DatabaseConstants.PREFERENCES_COL_DEVICE_ID, deviceId), preferencesExtractor);
    }
}
