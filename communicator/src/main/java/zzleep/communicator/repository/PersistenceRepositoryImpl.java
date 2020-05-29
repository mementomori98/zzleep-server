package zzleep.communicator.repository;


import org.springframework.web.client.HttpClientErrorException;
import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import org.springframework.stereotype.Component;
import zzleep.core.logging.Logger;
import zzleep.core.repositories.Context;

import java.util.ArrayList;
import java.util.List;


@Component
public class PersistenceRepositoryImpl implements PersistenceRepository {
    private static final String SLEEP_TABLE = "datamodels.sleep";
    private static final String ACTIVE_SLEEP_TABLE ="datamodels.activesleeps";
    private static final String ROOM_C_TABLE = "datamodels.roomConditions";
    private static final String PREFERENCE_TABLE = "datamodels.preferences";
    private static final String ACTIVE_VENTILATION_TABLE = "datamodels.activeventilation";
    private static final String COL_REGULATION_ENABLE = "regulationenabled";
    private static final String JOIN_PREFERENCES = "join datamodels.preferences on co2 < preferences.co2max " +
                                                    "AND humidity between humiditymin and humiditymax " +
                                                    "AND temperature between temperaturemin and temperaturemax";
    private static final String JOIN_ACTIVE_VENTILATION ="join datamodels.activeventilation on preferences.deviceid = activeventilation.deviceid";

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
    private static final String JOIN_EXCEPT = "left outer join datamodels.activesleeps on sleep.sleepid = activesleeps.sleepid";

    private Context context;
    private Logger logger;

    private static final Context.ResultSetExtractor<String> SLEEP_ID_EXTRACTOR = row-> ""+row.getInt(COL_SLEEP_ID);
    private static final Context.ResultSetExtractor<String> DEVICE_ID_EXTRACTOR = row-> ""+row.getString(COL_DEVICE_ID);

    public PersistenceRepositoryImpl(Context context, Logger logger)
    {
        this.context = context;
        this.logger = logger;
    }

    @Override
    public void putDataInDatabase(CurrentData data){

        try
        {
            String sleepId = context.single(SLEEP_TABLE, String.format("%s is null and %s =%s",COL_FINISH_TIME, COL_DEVICE_ID,"'"+ data.getSource()+"'"), SLEEP_ID_EXTRACTOR);

            if(sleepId != null)
            {

                String columns = COL_SLEEP_ID+", "+COL_TIMESTAMP+", "+COL_TEMPERATURE+", "+COL_CO2+", "+COL_SOUND+", "+COL_HUMIDITY;
                String values = sleepId +", "+ "'"+data.getTimeStamp()+"'" +", "+data.getTemperatureData()+", "+data.getCo2Data()+", "+data.getSoundData()+", "+data.getHumidityData();
                context.insert(ROOM_C_TABLE,columns, values, SLEEP_ID_EXTRACTOR);
            }
            else
            {
                logger.warn("Warning.SleepId is null");
            }

        }catch(HttpClientErrorException e)
        {
            logger.error("You are trying to introduce invalid format data ");

        }

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


        ArrayList<String> sources = new ArrayList<>();
        List<String> deviceIds = context.select(PREFERENCE_TABLE +" "+ JOIN_ACTIVE_VENTILATION, String.format("%s is true and %s.%s in (select %s from %s)", COL_REGULATION_ENABLE, PREFERENCE_TABLE, COL_DEVICE_ID, COL_DEVICE_ID, ACTIVE_VENTILATION_TABLE), DEVICE_ID_EXTRACTOR);

        for (String id: deviceIds) {
            String sleepId = context.single(SLEEP_TABLE, String.format("%s = '%s' and %s is null", COL_DEVICE_ID,id,COL_TIMESTAMP ), SLEEP_ID_EXTRACTOR);
            if(sleepId != null)
            {
                List<String> sleepIdsForGoodRoomConditions = context.select(ROOM_C_TABLE + " "+JOIN_PREFERENCES, String.format("%s = %s and %s > now() - '15 minutes'::interval", COL_SLEEP_ID, sleepId, COL_TIMESTAMP), SLEEP_ID_EXTRACTOR);

                if(sleepIdsForGoodRoomConditions.size()>3)
                {
                    sources.add(id);
                    context.delete(ACTIVE_VENTILATION_TABLE, String.format("%s =%s", COL_DEVICE_ID, id));
                }
            }

        }

        return sources;

    }

    ArrayList<String> getStartVentilation() {

        ArrayList<String> sources = new ArrayList<>();
        List<String> deviceIds = context.select(PREFERENCE_TABLE +" "+ JOIN_ACTIVE_VENTILATION, String.format("%s is true and %s.%s not in (select %s from %s)", COL_REGULATION_ENABLE, PREFERENCE_TABLE, COL_DEVICE_ID, COL_DEVICE_ID, ACTIVE_VENTILATION_TABLE), DEVICE_ID_EXTRACTOR);

        for (String id: deviceIds) {
           String sleepId = context.single(SLEEP_TABLE, String.format("%s = '%s' and %s is null", COL_DEVICE_ID,id,COL_TIMESTAMP ), SLEEP_ID_EXTRACTOR);
           if(sleepId != null)
           {
               List<String> sleepIdsForGoodRoomConditions = context.select(ROOM_C_TABLE + " "+JOIN_PREFERENCES, String.format("%s = %s and %s > now() - '15 minutes'::interval", COL_SLEEP_ID, sleepId, COL_TIMESTAMP), SLEEP_ID_EXTRACTOR);

               if(sleepIdsForGoodRoomConditions.size()<3)
               {
                   sources.add(id);
                   context.insert(ACTIVE_VENTILATION_TABLE, COL_DEVICE_ID, id, DEVICE_ID_EXTRACTOR);
               }
           }

        }

        return sources;
    }

    ArrayList<String> getStoppedSleeps() {

        List<String> sleepIds = context.select(SLEEP_TABLE +" "+ JOIN_AS, String.format("%s is not null", COL_FINISH_TIME), SLEEP_ID_EXTRACTOR);
        for (String sleepId:sleepIds) {

            context.delete(ACTIVE_SLEEP_TABLE, String.format("%s = %s", COL_SLEEP_ID, sleepId));

        }

        ArrayList<String> sources = getSources(sleepIds);

        // TODO: 5/27/2020 Tell embedded that DO stops also ventilation if on 
        for (String source:sources) {
            context.delete(ACTIVE_VENTILATION_TABLE, String.format("%s ='%s'", COL_DEVICE_ID, source));
        }
        return sources;
    }



    ArrayList<String> getActiveSleeps() {

        List<String> sleepIds = context.selectExcept(SLEEP_TABLE +" "+JOIN_EXCEPT, String.format("%s is null", COL_FINISH_TIME),
                SLEEP_TABLE +" "+ JOIN_EXCEPT, String.format("%s is not null ", COL_ACTIVES_ID), SLEEP_ID_EXTRACTOR);

        for (String sleepId:sleepIds) {
            context.insert(ACTIVE_SLEEP_TABLE, COL_SLEEP_ID, sleepId, SLEEP_ID_EXTRACTOR);
        }
        return getSources(sleepIds);
    }

    ArrayList<String> getSources(List<String> sleepIds) {
        ArrayList<String> sources = new ArrayList<>();

        for (String sleepId:sleepIds) {

            if(isInteger(sleepId))
            {
                try{
                    String deviceId = context.single(SLEEP_TABLE, String.format("%s = %s", COL_SLEEP_ID, sleepId), DEVICE_ID_EXTRACTOR);
                    if (deviceId!=null)
                    {
                        sources.add(deviceId);

                    }
                }catch(HttpClientErrorException e)
                {
                    logger.error("You are trying to retrieve a device for a non existing or incorrect format of sleepId");

                }
            }


        }


        return sources;
    }


    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
