package zzleep.communicator.databaseService;

import org.springframework.web.client.HttpClientErrorException;
import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import org.springframework.stereotype.Component;
import zzleep.core.repositories.Context;


import java.util.ArrayList;
import java.util.List;


@Component
public class DatabaseServiceImpl implements DatabaseService{
    private static final String SLEEP_TABLE = "datamodels.sleep";
    private static final String ACTIVE_SLEEP_TABLE ="datamodels.activesleeps";
    private static final String ROOM_C_TABLE = "datamodels.roomConditions";
    private static final String COL_ACTIVES_ID ="activesleeps.sleepid";
    private static final String COL_SLEEP_ID = "sleepid";
    private static final String COL_DEVICE_ID = "deviceid";
    private static final String COL_TIMESTAMP = "time";
    private static final String COL_TEMPERATURE ="temperature";
    private static final String COL_CO2 =  "co2";
    private static final String COL_SOUND = "sound";
    private static final String COL_HUMIDITY = "humidity";
    private static final String COL_FINISH_TIME = "datetimeend";
    private static final String JOIN_S = "join datamodels.device on sleep.deviceid = deviceid"; ////////////
    private static final String JOIN_AS = "join datamodels.activesleeps on sleep.sleepid = activesleeps.sleepid ";
    private static final String JOIN_EXCEPT = "left outer join datamodels.activesleeps on sleep.sleepid = activesleeps.sleepid";

    private Context context;

    private static final Context.ResultSetExtractor<String> sleepId_extractor = row-> ""+row.getInt(COL_SLEEP_ID);
    private static final Context.ResultSetExtractor<String> deviceId_extractor = row-> ""+row.getString(COL_DEVICE_ID);

    public DatabaseServiceImpl(Context context)
    {
        this.context = context;
    }

    @Override
    public void putDataInDatabase(CurrentData data){

        try
        {
            String sleepId = context.single(SLEEP_TABLE, String.format("%s is null and %s =%s",COL_FINISH_TIME, COL_DEVICE_ID,"'"+ data.getSource()+"'"), sleepId_extractor);

            String columns = COL_SLEEP_ID+", "+COL_TIMESTAMP+", "+COL_TEMPERATURE+", "+COL_CO2+", "+COL_SOUND+", "+COL_HUMIDITY;
            String values = sleepId +", "+ "'"+data.getTimeStamp()+"'" +", "+data.getTemperatureData()+", "+data.getCo2Data()+", "+data.getSoundData()+", "+data.getHumidityData();
            context.insert(ROOM_C_TABLE,columns, values, sleepId_extractor);
        }catch(HttpClientErrorException e)
        {
            System.out.println("You are trying to introduce invalid format data ");
            System.out.println("or");
            System.out.println("Something else");
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

    private ArrayList<String> getStopVentilation() {
        return new ArrayList<String>();
    }

    private ArrayList<String> getStartVentilation() {
        return new ArrayList<String>();
    }

    public ArrayList<String> getStoppedSleeps() {

        List<String> sleep_Ids = context.select(SLEEP_TABLE +" "+ JOIN_AS, String.format("%s is not null", COL_FINISH_TIME), sleepId_extractor);
        for (String sleep_id:sleep_Ids) {

            context.delete(ACTIVE_SLEEP_TABLE, String.format("%s = %s", COL_SLEEP_ID, sleep_id));
        }

        return getSources(sleep_Ids);
    }



    public ArrayList<String> getActiveSleeps() {

        List<String> sleep_Ids = context.selectExcept(SLEEP_TABLE +" "+JOIN_EXCEPT, String.format("%s is null", COL_FINISH_TIME),
                SLEEP_TABLE +" "+ JOIN_EXCEPT, String.format("%s is not null ", COL_ACTIVES_ID), sleepId_extractor);

        for (String sleep_id:sleep_Ids) {
            context.insert(ACTIVE_SLEEP_TABLE, COL_SLEEP_ID, sleep_id, sleepId_extractor);
        }
        return getSources(sleep_Ids);
    }

    public ArrayList<String> getSources(List<String> sleep_Ids) {
        ArrayList<String> sources = new ArrayList<>();

        for (String sleep_id:sleep_Ids) {

            try{
                String deviceId = context.single(SLEEP_TABLE, String.format("%s = %s", COL_SLEEP_ID, sleep_id), deviceId_extractor);
                sources.add(deviceId);
            }catch(HttpClientErrorException e)
            {
                System.out.println("You are trying to retrieve a device for a non existing or incorrect format of sleepId");
                System.out.println("or");
                System.out.println("Something else");
            }

        }


        return sources;
    }

}
