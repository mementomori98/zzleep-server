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


@Component
public class PersistenceRepositoryImpl implements PersistenceRepository {
/*    private static final String SLEEP_TABLE = "datamodels.sleep";
    private static final String ACTIVE_SLEEP_TABLE ="datamodels.activesleeps";
    private static final String ROOM_C_TABLE = "datamodels.roomConditions";
    private static final String PREFERENCE_TABLE = "datamodels.preferences";
    private static final String ACTIVE_VENTILATION_TABLE = "datamodels.activeventilation";
    private static final String COL_REGULATION_ENABLE = "regulationenabled";
    private static final String JOIN_PREFERENCES = "join datamodels.preferences on co2 < preferences.co2max " +
                                                    "AND humidity between humiditymin and humiditymax " +
                                                    "AND temperature between temperaturemin and temperaturemax";
    private static final String JOIN_ACTIVE_VENTILATION ="full join datamodels.activeventilation on preferences.deviceid = activeventilation.deviceid";

    private static final String COL_ACTIVES_ID ="activesleeps.sleepid";
    private static final String COL_SLEEP_ID = "sleepid";
    private static final String COL_DEVICE_ID = "deviceid";
    private static final String COL_TIMESTAMP = "time";
    private static final String COL_TEMPERATURE ="temperature";
    private static final String COL_CO2 =  "co2";
    private static final String COL_SOUND = "sound";
    private static final String COL_HUMIDITY = "humidity";
    private static final String COL_FINISH_TIME = "datetimeend";
    private static final String JOIN_AS = "join datamodels.activesleeps on sleep.sleepid = activesleeps.sleepid ";
    private static final String LEFT_OUTER_JOIN = "left outer join datamodels.activesleeps on sleep.sleepid = activesleeps.sleepid";*/
    private static final String LEFT_OUTER_JOIN_STRING = "%s left outer join %s on %s";

    private Context context;
    private Logger logger;

    private static final Context.ResultSetExtractor<Integer> sleepIdExtractor = ExtractorFactory.getSleepIdExtractor();
    private static final Context.ResultSetExtractor<String> DEVICE_ID_EXTRACTOR = ExtractorFactory.getDeviceIdExtractor();

    public PersistenceRepositoryImpl(Context context, Logger logger)
    {
        this.context = context;
        this.logger = logger;
    }

    @Override
    public void putDataInDatabase(CurrentData data){

        Integer sleepId = getSleepId(data.getSource());

        if(sleepId != null)
            insertRoomConditions(data, sleepId);
        else
            logger.warn("Warning.SleepId is null");
        //there was a try catch here with HTTpClientErrorException
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
        String values = String.format(
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

    @Override
    public ArrayList<Command> getUpdates() {
        ArrayList<Command> commands = new ArrayList<>();

        ArrayList<String> startDevices = getActiveSleeps();
        for (String source : startDevices) {
            Command command = new Command(source, 'D', 1);
            commands.add(command);
        }

        ArrayList<String> stopDevices = getStoppedSleeps();
        for (String source : stopDevices) {
            Command command = new Command(source, 'D', 0);
            commands.add(command);
        }

        ArrayList<String> startVentilation =getStartVentilation();
        for (String source : startVentilation) {
            Command command = new Command(source, 'V', 1);
            commands.add(command);
        }

        ArrayList<String> stopVentilation = getStopVentilation();
        for (String source : stopVentilation) {
            Command command = new Command(source, 'V', 0);
            commands.add(command);
        }

        return commands;
    }

    ArrayList<String> getStopVentilation() {


        /*ArrayList<String> sources = new ArrayList<>();
        List<String> deviceIds = context.select(PREFERENCE_TABLE +" "+ JOIN_ACTIVE_VENTILATION, String.format("%s is true and %s.%s in (select %s from %s)", COL_REGULATION_ENABLE, PREFERENCE_TABLE, COL_DEVICE_ID, COL_DEVICE_ID, ACTIVE_VENTILATION_TABLE), DEVICE_ID_EXTRACTOR);

        for (String id: deviceIds) {
            String sleepId = context.single(SLEEP_TABLE, String.format("%s = '%s' and %s is null", COL_DEVICE_ID,id,COL_TIMESTAMP ), SLEEP_ID_EXTRACTOR);
            if(sleepId != null)
            {
                List<String> sleepIdsForGoodRoomConditions = context.select(ROOM_C_TABLE + " "+JOIN_PREFERENCES, String.format("%s = %s and %s.%s ='%s' and %s > now() - '15 minutes'::interval", COL_SLEEP_ID, sleepId,PREFERENCE_TABLE, COL_DEVICE_ID, id,  COL_TIMESTAMP), SLEEP_ID_EXTRACTOR);

                if(sleepIdsForGoodRoomConditions.size()>3)
                {
                    sources.add(id);
                    context.delete(ACTIVE_VENTILATION_TABLE, String.format("%s = '%s'", COL_DEVICE_ID, id));
                }
            }

        }

        return sources;*/
return null;
    }

    ArrayList<String> getStartVentilation() {

        List<Sleep> sleeps = getActiveSleepsWhereRegulationIsEnabled();
        ArrayList<String> deviceIdsToRegulate = new ArrayList<>();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            if(needsRegulations(sleeps.get(i).getSleepId()))
            {
                deviceIdsToRegulate.add(sleeps.get(i).getDeviceId());
                context.insert(DatabaseConstants.ACTIVE_VENTILATION_TABLE, DatabaseConstants.ACTIVE_VENTILATION_COL_DEVICE_ID, sleeps.get(i).getDeviceId(), DEVICE_ID_EXTRACTOR);
            }
        }
        return deviceIdsToRegulate;
        /*ArrayList<String> sources = new ArrayList<>();
        List<String> deviceIds = context.select(PREFERENCE_TABLE +" "+ JOIN_ACTIVE_VENTILATION, String.format("%s is true and %s.%s not in (select %s from %s)", COL_REGULATION_ENABLE, PREFERENCE_TABLE, COL_DEVICE_ID, COL_DEVICE_ID, ACTIVE_VENTILATION_TABLE), DEVICE_ID_EXTRACTOR);

        for (String id: deviceIds) {
           String sleepId = context.single(SLEEP_TABLE, String.format("%s = '%s' and %s is null", COL_DEVICE_ID,id,COL_FINISH_TIME ), SLEEP_ID_EXTRACTOR);
           if(sleepId != null)
           {
               List<String> sleepIdsForGoodRoomConditions = context.select(ROOM_C_TABLE + " "+JOIN_PREFERENCES, String.format("%s = %s and %s.%s ='%s' and %s > now() - '15 minutes'::interval", COL_SLEEP_ID, sleepId,PREFERENCE_TABLE, COL_DEVICE_ID, id,  COL_TIMESTAMP), SLEEP_ID_EXTRACTOR);

               if(sleepIdsForGoodRoomConditions.size()<3)
               {
                   sources.add(id);
                   context.insert(ACTIVE_VENTILATION_TABLE, COL_DEVICE_ID, id, DEVICE_ID_EXTRACTOR);
               }
           }

        }

        return sources;*/
    }

    private boolean needsRegulations(int sleepId)
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
        //String activeSleepsQuery = String.format("select %s from %s", DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID, DatabaseConstants.ACTIVE_SLEEPS_TABLE);
        String condition = String.format(
                "%s.%s < %s.%s and %s.%s > %s.%s and %s.%s < %s.%s and %s.%s > %s.%s and %s.%s < %s.%s and %s.%s > now - '15 minutes'::interval and %s.%s = %d",
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
                DatabaseConstants.RC_COL_TIME,
                DatabaseConstants.SLEEP_TABLE_NAME,
                DatabaseConstants.SLEEP_COL_SLEEP_ID,
                sleepId
        );
        if(context.select(bigJoinTable,
                condition,
                sleepIdExtractor).size() < 3) return true;
        return false;
    }

    private List<Sleep> getActiveSleepsWhereRegulationIsEnabled()
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

    ArrayList<String> getStoppedSleeps() {

        List<Sleep> sleeps = getFinishedSleeps();
        removeFromActiveSleeps(sleeps);
        ArrayList<String> deviceIds = getDeviceIds(sleeps);
        removeVentilationFromDatabase(deviceIds);
        return deviceIds;
    }

    private List<Sleep> getFinishedSleeps()
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

    private void removeFromActiveSleeps(List<Sleep> sleeps)
    {
        for(int i = 0; i < sleeps.size(); ++i)
            context.delete(DatabaseConstants.ACTIVE_SLEEPS_TABLE, String.format("%s = %d", DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID, sleeps.get(i).getSleepId()));
    }

    private void removeVentilationFromDatabase(List<String> deviceIds)
    {
        for(int i = 0; i < deviceIds.size(); ++i)
            context.delete(DatabaseConstants.ACTIVE_VENTILATION_TABLE, String.format("%s ='%s'", DatabaseConstants.ACTIVE_VENTILATION_COL_DEVICE_ID, deviceIds));
    }

    ArrayList<String> getActiveSleeps() {
        List<Sleep> newSleeps = getNewSleeps();
        insertNewSleepsInActiveSleeps(newSleeps);
        return getDeviceIds(newSleeps);
    }

    private List<Sleep> getNewSleeps()
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

    private void insertNewSleepsInActiveSleeps(List<Sleep> sleeps)
    {
        for(int i = 0; i < sleeps.size(); ++i)
        {
            context.insert(
                    DatabaseConstants.ACTIVE_SLEEPS_TABLE,
                    DatabaseConstants.ACTIVE_SLEEPS_COL_SLEEP_ID,
                    "" + sleeps.get(i).getSleepId(),
                    sleepIdExtractor);
        }
    }

    private ArrayList<String> getDeviceIds(List<Sleep> sleeps)
    {
        ArrayList<String> deviceIds = new ArrayList<>();
        for(int i = 0; i < sleeps.size(); ++i)
            deviceIds.add(sleeps.get(i).getDeviceId());
        return deviceIds;
    }
}
