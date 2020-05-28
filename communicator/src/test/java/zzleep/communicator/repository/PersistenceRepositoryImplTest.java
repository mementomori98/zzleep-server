package zzleep.communicator.repository;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import zzleep.core.logging.Logger;
import zzleep.core.logging.LoggerImpl;
import zzleep.core.repositories.Context;
import zzleep.core.repositories.PostgresContext;

import java.util.ArrayList;
import java.util.List;


public class PersistenceRepositoryImplTest {

    private static PersistenceRepositoryImpl dbService;

    @BeforeClass
    public static void setUp()
    {
        RestTemplateBuilder restTemplate =  new RestTemplateBuilder();
        Logger logger = new LoggerImpl(restTemplate);
        Context context = new PostgresContext(logger);
        dbService = new PersistenceRepositoryImpl(context);
    }

    @Test
    public void getStartVentilation()
    {
        ArrayList<String> sources = dbService.getStartVentilation();
        for (String source:
             sources) {
            System.out.println("v1:"+source);
        }
    }

    @Test
    public void getStopVentilation()
    {
        ArrayList<String> sources = dbService.getStopVentilation();
        for (String source:
                sources) {
            System.out.println("v0:"+source);
        }
    }

    @Test
    public void putDataInDatabase() {

        CurrentData data = new CurrentData();
        data.setSource("0004A30B002181EC");
        data.setTimeStamp("2020-05-21 16:10:55");
        data.setTemperatureData(27.0);
        data.setHumidityData(50.0);
        data.setCo2Data(250.0);
        data.setSoundData(30.0);
        dbService.putDataInDatabase(data);


    }

    @Test
    public void putWrongSourceFormatInDatabase()
    {
        CurrentData data = new CurrentData();
        data.setSource("NonExistingSource");
        data.setTimeStamp("2020-05-21 14:25:55");
        data.setTemperatureData(27.0);
        data.setHumidityData(50.0);
        data.setCo2Data(250.0);
        data.setSoundData(30.0);
        dbService.putDataInDatabase(data);

    }

    @Test
    public void putWrongTimeStampFormatInDatabase()
    {
        CurrentData data = new CurrentData();
        data.setSource("0004A30B002181EC");
        data.setTimeStamp("wrongFormatOfDate");
        data.setTemperatureData(27.0);
        data.setHumidityData(50.0);
        data.setCo2Data(250.0);
        data.setSoundData(30.0);
        dbService.putDataInDatabase(data);
    }

    @Test
    public void putWrongTemperatureFormatInDatabase()
    {
        CurrentData data = new CurrentData();
        data.setSource("0004A30B002181EC");
        data.setTimeStamp("2020-05-21 14:25:55");
        data.setTemperatureData(87.8);
        data.setHumidityData(50.0);
        data.setCo2Data(250.0);
        data.setSoundData(30.0);
        dbService.putDataInDatabase(data);
    }

    @Test
    public void putWrongHumidityFormatInDatabase()
    {
        CurrentData data = new CurrentData();
        data.setSource("0004A30B002181EC");
        data.setTimeStamp("2020-05-21 14:25:55");
        data.setTemperatureData(27.0);
        data.setHumidityData(-34.8);
        data.setCo2Data(250.0);
        data.setSoundData(30.0);
        dbService.putDataInDatabase(data);
    }

    @Test
    public void putWrongCo2FormatInDatabase()
    {
        CurrentData data = new CurrentData();
        data.setSource("0004A30B002181EC");
        data.setTimeStamp("2020-05-21 14:25:55");
        data.setTemperatureData(27.0);
        data.setHumidityData(50.0);
        data.setCo2Data(150.0);
        data.setSoundData(30.0);
        dbService.putDataInDatabase(data);
    }

    @Test
    public void putWrongSoundFormatInDatabase()
    {
        CurrentData data = new CurrentData();
        data.setSource("0004A30B002181EC");
        data.setTimeStamp("2020-05-21 14:25:55");
        data.setTemperatureData(27.0);
        data.setHumidityData(50.0);
        data.setCo2Data(250.0);
        data.setSoundData(-56.9);
        dbService.putDataInDatabase(data);
    }

    @Test
    public void getUpdates() {

        ArrayList<Command> commands = dbService.getUpdates();

        for (Command com: commands) {
            System.out.println(com.toString());
        }
    }

    @Test
    public void getStoppedSleeps() {

        ArrayList<String> sources = dbService.getStoppedSleeps();

        for (String source: sources) {
            System.out.println("3: "+source);
        }
    }

    @Test
    public void getActiveSleeps() {


       ArrayList<String> sources = dbService.getActiveSleeps();

        for (String source: sources) {
            System.out.println("2: "+source);
        }

    }

    @Test
    public void getSources() {

        String sleepDumb = "3";
        String sleepDevice ="11";

        List<String> list = new ArrayList<>();
        list.add(sleepDumb);
        list.add(sleepDevice);

        ArrayList<String> sources = dbService.getSources(list);
        for (String deviceId:sources) {
            System.out.println("1: "+deviceId);
        }

    }

    @Test
    public void getSourcesForNonExistingSleeps()
    {
        String nonExistingSleepAtTheMomentOfTesting = "30";
        List<String> list = new ArrayList<>();
        list.add(nonExistingSleepAtTheMomentOfTesting);

        ArrayList<String> sources = dbService.getSources(list);
        for (String deviceId:sources) {
            System.out.println("0: "+deviceId);
        }

    }

    @Test
    public void getSourcesOnIncorrectFormatOfSleepId()
    {
        String nonExistingSleepAtTheMomentOfTesting = "abc";
        List<String> list = new ArrayList<>();
        list.add(nonExistingSleepAtTheMomentOfTesting);

        ArrayList<String> sources = dbService.getSources(list);
        for (String deviceId:sources) {
            System.out.println("0: "+deviceId);
        }
    }




}