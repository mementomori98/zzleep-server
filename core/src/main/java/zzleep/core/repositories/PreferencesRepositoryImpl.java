package zzleep.core.repositories;

import org.springframework.stereotype.Component;
import zzleep.core.models.Device;
import zzleep.core.models.Preferences;

@Component
public class PreferencesRepositoryImpl implements PreferencesRepository {
    private Context context;

    private final DeviceRepository deviceRepository;

    private Context.ResultSetExtractor<Preferences> preferencesExtractor = ExtractorFactory.getPreferencesExtractor();
    private Context.ResultSetExtractor<Device> deviceExtractor = ExtractorFactory.getDeviceExtractor();

    public PreferencesRepositoryImpl(Context context, DeviceRepository deviceRepository) {
        this.context = context;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Preferences setPreferences(Preferences preferences) throws InvalidValuesException {
        if (!preferences.isValid()) throw new InvalidValuesException();
        if (!deviceRepository.exists(preferences.getDeviceId())) return null;
        if (exists(preferences.getDeviceId()))
            return create(preferences);
        return update(preferences);
    }

    private boolean exists(String deviceId) {
        return context.single(
            DatabaseConstants.PREFERENCES_TABLE_NAME,
            String.format("%s = '%s'", DatabaseConstants.PREFERENCES_COL_DEVICE_ID, deviceId),
            preferencesExtractor
        ) != null;
    }

    private Preferences update(Preferences preferences) {
        return context.update(DatabaseConstants.PREFERENCES_TABLE_NAME,
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
            preferencesExtractor
        );
    }

    private Preferences create(Preferences preferences) {
        return context.insert(DatabaseConstants.PREFERENCES_TABLE_NAME,
            String.format("%s, %s, %s, %s, %s, %s, %s",
                DatabaseConstants.PREFERENCES_COL_DEVICE_ID,
                DatabaseConstants.PREFERENCES_COL_REGULATIONS_ENABLED,
                DatabaseConstants.PREFERENCES_COL_CO2_MAX,
                DatabaseConstants.PREFERENCES_COL_HUMIDITY_MIN,
                DatabaseConstants.PREFERENCES_COL_HUMIDITY_MAX,
                DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MIN,
                DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MAX),
            String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                preferences.getDeviceId(),
                preferences.isRegulationEnabled(),
                preferences.getCo2Max(),
                preferences.getHumidityMin(),
                preferences.getHumidityMax(),
                preferences.getTemperatureMin(),
                preferences.getTemperatureMax()),
            preferencesExtractor
        );
    }

    @Override
    public Preferences getPreferences(String deviceId) {
        return context.single(
            DatabaseConstants.PREFERENCES_TABLE_NAME,
            String.format("%s = '%s'", DatabaseConstants.PREFERENCES_COL_DEVICE_ID, deviceId),
            preferencesExtractor
        );
    }
}
