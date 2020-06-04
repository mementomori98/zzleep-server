package zzleep.communicator.repository;

import zzleep.core.models.Sleep;
import zzleep.core.repositories.DatabaseConstants;
import zzleep.core.repositories.ExtractorFactory;
import org.springframework.web.client.HttpClientErrorException;
import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import org.springframework.stereotype.Component;
import zzleep.core.logging.Logger;
import zzleep.core.repositories.Context;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Component
public class PersistenceRepositoryImpl implements PersistenceRepository {

    private Context context;
    private Logger logger;

    private static final Context.ResultSetExtractor<Integer> sleepIdExtractor = ExtractorFactory.getSleepIdExtractor();
    private static final Context.ResultSetExtractor<String> deviceIdExtractor = ExtractorFactory.getDeviceIdExtractor();

    public PersistenceRepositoryImpl(Context context, Logger logger)
    {
        this.context = context;
        this.logger = logger;
    }

    @Override
    public void putDataInDatabase(CurrentData data){

        Integer sleepId = getSleepId(data.getSource());

        if(sleepId != null)
        {
            insertRoomConditions(checkConstraints(data), sleepId);
        }
        else
            try {
                logger.warn("Warning.SleepId is null");
            }
            catch(HttpClientErrorException e)
            {

            }
    }

    private Integer getSleepId(String deviceId)
    {
        return context.single(
                DatabaseConstants.SLEEP_TABLE_NAME,
                String.format("%s is null and %s = '%s'",
                        DatabaseConstants.SLEEP_COL_FINISH_TIME,
                        DatabaseConstants.SLEEP_COL_DEVICE_ID,
                        deviceId),
                sleepIdExtractor
        );
    }

    private void insertRoomConditions(CurrentData data, int sleepId)
    {
        String columns = String.format(
                "%s, %s, %s, %s, %s, %s",
                DatabaseConstants.RC_COL_SLEEP_ID,
                DatabaseConstants.RC_COL_TIME,
                DatabaseConstants.RC_COL_TEMPERATURE,
                DatabaseConstants.RC_COL_CO2,
                DatabaseConstants.RC_COL_SOUND,
                DatabaseConstants.RC_COL_HUMIDITY
        );
        String values = String.format(Locale.ROOT,
                "%d, '%s', %d, %d, %.2f, %.2f",
                sleepId,
                data.getTimeStamp(),
                data.getTemperatureData(),
                data.getCo2Data(),
                data.getSoundData(),
                data.getHumidityData()
        );
        context.insert(DatabaseConstants.RC_TABLE_NAME,columns, values, sleepIdExtractor);
    }

    private CurrentData checkConstraints(CurrentData data)
    {
        if(data.getCo2Data() < 200 || data.getCo2Data() > 10000)
            data.setCo2Data(null);
        if(data.getTemperatureData() < -40 || data.getTemperatureData() > 55)
            data.setTemperatureData(null);
        if(data.getHumidityData() < 0 || data.getHumidityData() > 100)
            data.setHumidityData(null);
        if(data.getSoundData() < 0 || data.getSoundData() > 150)
            data.setSoundData(null);
        return data;
    }

    public void insertVentilationInDb(String deviceId)
    {
        context.insert(
                DatabaseConstants.ACTIVE_VENTILATION_TABLE,
                DatabaseConstants.ACTIVE_VENTILATION_COL_DEVICE_ID,
                String.format("'%s'", deviceId),
                deviceIdExtractor);
    }

    public void deleteVentilationFromDb(String deviceId)
    {
        context.delete(
                DatabaseConstants.ACTIVE_VENTILATION_TABLE,
                String.format(
                        "%s = '%s'",
                        DatabaseConstants.ACTIVE_VENTILATION_COL_DEVICE_ID,
                        deviceId
                )
        );
    }

    public String getDeviceIdFromActiveVentilations(String deviceId)
    {
        return context.single(DatabaseConstants.ACTIVE_VENTILATION_TABLE,
                String.format(
                        "%s = '%s'",
                        DatabaseConstants.ACTIVE_VENTILATION_COL_DEVICE_ID,
                        deviceId),
                deviceIdExtractor);
    }

    public int getCountOfLatestGoodValues(int sleepId)
    {
        String bigJoinTable = String.format(
                "%s join %s on %s.%s = %s.%s join %s on %s.%s = %s.%s",
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.SLEEP_TABLE_NAME,
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_SLEEP_ID,
                DatabaseConstants.SLEEP_TABLE_NAME,
                DatabaseConstants.SLEEP_COL_SLEEP_ID,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_DEVICE_ID,
                DatabaseConstants.SLEEP_TABLE_NAME,
                DatabaseConstants.SLEEP_COL_DEVICE_ID
         );

        String condition = String.format(
                "%s.%s < %s.%s and %s.%s > %s.%s and %s.%s < %s.%s and %s.%s > %s.%s and %s.%s < %s.%s and %s.%s > now() - '15 minutes'::interval and %s.%s = %d",
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_CO2,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_CO2_MAX,
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_HUMIDITY,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_HUMIDITY_MIN,
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_HUMIDITY,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_HUMIDITY_MAX,
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_TEMPERATURE,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MIN,
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_TEMPERATURE,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_TEMPERATURE_MAX,
                DatabaseConstants.RC_TABLE_NAME,
                DatabaseConstants.RC_COL_TIME,
                DatabaseConstants.SLEEP_TABLE_NAME,
                DatabaseConstants.SLEEP_COL_SLEEP_ID,
                sleepId
        );
        List<Integer> sleeps = context.select(bigJoinTable,
                condition,
                sleepIdExtractor);
        return sleeps.size();
    }

    public List<Sleep> getActiveSleepsWhereRegulationIsEnabled()
    {
        String regulationEnabledDeviceId = String.format("select %s from %s where %s is true",
                DatabaseConstants.PREFERENCES_COL_DEVICE_ID,
                DatabaseConstants.PREFERENCES_TABLE_NAME,
                DatabaseConstants.PREFERENCES_COL_REGULATIONS_ENABLED);
        String activeSleepsIds = String.format("select %s from %s", DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID, DatabaseConstants.ACTIVE_SLEEPS_TABLE);
        String condition = String.format(
                "%s in (%s) and %s in (%s)",
                DatabaseConstants.SLEEP_COL_SLEEP_ID,
                activeSleepsIds,
                DatabaseConstants.SLEEP_COL_DEVICE_ID,
                regulationEnabledDeviceId
        );
        return context.select(
                DatabaseConstants.SLEEP_TABLE_NAME,
                condition,
                ExtractorFactory.getSleepExtractor()
                );
    }

    public List<Sleep> getFinishedSleeps()
    {
        String selectFromActiveSleeps = String.format(
                "select %s from %s",
                DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID,
                DatabaseConstants.ACTIVE_SLEEPS_TABLE);

        String condition = String.format(
                "%s is not null and %s in (%s)",
                DatabaseConstants.SLEEP_COL_FINISH_TIME,
                DatabaseConstants.SLEEP_COL_SLEEP_ID,
                selectFromActiveSleeps);
        return context.select(DatabaseConstants.SLEEP_TABLE_NAME, condition, ExtractorFactory.getSleepExtractor());
    }

    public void removeActiveSleep(int sleepId)
    {
        context.delete(
                DatabaseConstants.ACTIVE_SLEEPS_TABLE,
                String.format(
                        "%s = %d",
                        DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID, sleepId
                )
        );
    }

    public List<Sleep> getNewSleeps()
    {
        String selectFromActiveSleeps = String.format(
                "select %s from %s",
                DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID,
                DatabaseConstants.ACTIVE_SLEEPS_TABLE);

        String condition = String.format(
                "%s is null and %s not in (%s)",
                DatabaseConstants.SLEEP_COL_FINISH_TIME,
                DatabaseConstants.SLEEP_COL_SLEEP_ID,
                selectFromActiveSleeps);
        return context.select(DatabaseConstants.SLEEP_TABLE_NAME, condition, ExtractorFactory.getSleepExtractor());
    }

    public void insertInActiveSleeps(int sleepId)
    {
        context.insert(
                DatabaseConstants.ACTIVE_SLEEPS_TABLE,
                DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID,
                "" + sleepId,
                sleepIdExtractor);
    }


}
