package zzleep.communicator.databaseService;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import org.springframework.stereotype.Component;
import zzleep.core.repositories.Context;


import java.util.ArrayList;
import java.util.List;


@Component
public class DatabaseServiceImpl implements DatabaseService{
    private static final String SLEEP_TABLE = "sleep";
    private static final String ACTIVE_SLEEP_TABLE ="activeSleep";
    private static final String ROOM_C_TABLE = "roomConditions";
    private static final String COL_SLEEP_ID = "sleepId";
    private static final String COL_DEVICE_ID = "deviceId";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_TEMPERATURE ="temperature";
    private static final String COL_CO2 =  "co2";
    private static final String COL_SOUND = "sound";
    private static final String COL_HUMIDITY = "humidity";
    private static final String COL_FINISH_TIME = "dateTimeEnd";
    private static final String JOIN_S = "join device on sleep.deviceId = deviceId";
    private static final String JOIN_AS = "join activeSleep on sleep.sleepId = activeSleep.sleepId ";

    private Context context;

    private static final Context.ResultSetExtractor<String> sleepId_extractor = row-> ""+row.getInt(COL_SLEEP_ID);
    private static final Context.ResultSetExtractor<String> deviceId_extractor = row-> ""+row.getString(COL_DEVICE_ID);

    public DatabaseServiceImpl(Context context)
    {
        this.context = context;
    }

    @Override
    public void putDataInDatabase(CurrentData data){
        

        String sleepId = context.single(SLEEP_TABLE, String.format("%s = null %s", COL_FINISH_TIME, JOIN_S), sleepId_extractor);
//        if(sleepId == null)
//        {
//            throw new SleepRepository.SleepNotStartedException();
//        }
        String columns = COL_SLEEP_ID+", "+COL_TIMESTAMP+", "+COL_TEMPERATURE+", "+COL_CO2+", "+COL_SOUND+", "+COL_HUMIDITY;
        String values = sleepId +", "+data.getTimeStamp()+", "+data.getTemperatureData()+", "+data.getCo2Data()+", "+data.getSoundData()+", "+data.getHumidityData();
        context.insert(ROOM_C_TABLE,columns, values, sleepId_extractor);

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

    private ArrayList<String> getStopVentilation() {
        return new ArrayList<String>();
    }

    private ArrayList<String> getStartVentilation() {
        return new ArrayList<String>();
    }

    private ArrayList<String> getStoppedSleeps() {

        List<String> sleep_Ids = context.select(SLEEP_TABLE, String.format("%s != null %s", COL_FINISH_TIME, JOIN_AS), sleepId_extractor);
        for (String sleep_id:sleep_Ids) {

            context.delete(ACTIVE_SLEEP_TABLE, String.format("%s = %s", COL_SLEEP_ID, sleep_id));
        }

        return getSources(sleep_Ids);
    }



    private ArrayList<String> getActiveSleeps() {

        List<String> sleep_Ids = context.selectExcept(SLEEP_TABLE, String.format("%s = null", COL_FINISH_TIME),
                SLEEP_TABLE, String.format("%s = null %s", COL_FINISH_TIME, JOIN_AS), sleepId_extractor);

        for (String sleep_id:sleep_Ids) {
            context.insert(ACTIVE_SLEEP_TABLE, COL_SLEEP_ID, sleep_id, sleepId_extractor);
        }
        return getSources(sleep_Ids);
    }

    private ArrayList<String> getSources(List<String> sleep_Ids) {
        ArrayList<String> sources = new ArrayList<>();

        for (String sleep_id:sleep_Ids) {
            String deviceId = context.single(SLEEP_TABLE, String.format("%s = %s", COL_SLEEP_ID, sleep_id), deviceId_extractor);
//            if(deviceId == null)
//            {
//                throw new SleepRepository.SleepNotStartedException();
//            }
            sources.add(deviceId);
        }


        return sources;
    }
}
