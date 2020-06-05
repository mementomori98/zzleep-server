package zzleep.communicator.repository;


import org.junit.*;

import zzleep.communicator.controller.CommandsServiceImpl;
import zzleep.communicator.models.Command;
import zzleep.core.models.Sleep;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class CommandsServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PersistenceRepository repo;

    private CommandsServiceImpl commandsHandler;

    @Before
    public void setUp() {
        commandsHandler = new CommandsServiceImpl(repo);
    }

    private ArrayList<Sleep> instantiateSleeps() {
        ArrayList<Sleep> sleeps = new ArrayList<>();
        Sleep s1 = new Sleep(1, "device1", LocalDateTime.of(2020, 6, 2, 23, 2), LocalDateTime.of(2020, 6, 3, 9, 55), 5);
        Sleep s2 = new Sleep(2, "device2", LocalDateTime.of(2020, 6, 2, 23, 2), LocalDateTime.of(2020, 6, 3, 9, 55), 5);
        Sleep s3 = new Sleep(3, "device3", LocalDateTime.of(2020, 6, 2, 23, 2), LocalDateTime.of(2020, 6, 3, 9, 55), 5);
        Sleep s4 = new Sleep(4, "device4", LocalDateTime.of(2020, 6, 2, 23, 2), LocalDateTime.of(2020, 6, 3, 9, 55), 5);
        Sleep s5 = new Sleep(5, "device5", LocalDateTime.of(2020, 6, 2, 23, 2), LocalDateTime.of(2020, 6, 3, 9, 55), 5);
        sleeps.add(s1);
        sleeps.add(s2);
        sleeps.add(s3);
        sleeps.add(s4);
        sleeps.add(s5);
        return sleeps;
    }

    //active sleeps
    private void activeSleepsSetup(List<Sleep> sleeps) {
        when(repo.getNewSleeps()).thenReturn(sleeps);
    }

    @Test
    public void testInsertActiveSleeps() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        commandsHandler.insertNewSleepsInActiveSleeps(sleeps);
        verify(repo, times(5)).insertInActiveSleeps(any(Integer.class));
    }

    @Test
    public void testGetActiveSleeps() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        activeSleepsSetup(sleeps);
        List<Sleep> sleeps2 = commandsHandler.getActiveSleeps();
        assertEquals(sleeps, sleeps2);
    }

    @Test
    public void testGetActiveSleepsEmptyList() {
        ArrayList<Sleep> sleeps = new ArrayList<>();
        activeSleepsSetup(sleeps);
        List<Sleep> sleeps2 = commandsHandler.getActiveSleeps();
        assertEquals(sleeps, sleeps2);
    }

    @Test
    public void testGetActiveSleepCommands() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        activeSleepsSetup(sleeps);

        ArrayList<Command> commands = commandsHandler.getActiveSleepCommands();

        assertEquals(commands.size(), sleeps.size());

        for (int i = 0; i < commands.size(); ++i)
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
    }

    @Test
    public void testGetActiveSleepCommandsEmptyList() {
        ArrayList<Sleep> sleeps = new ArrayList<>();
        when(repo.getNewSleeps()).thenReturn(sleeps);
        ArrayList<Command> commands = commandsHandler.getActiveSleepCommands();

        assertEquals(commands.size(), 0);
    }

    //stopped sleeps
    private void stoppedSleepsSetup(List<Sleep> sleeps) {
        when(repo.getFinishedSleeps()).thenReturn(sleeps);
    }

    @Test
    public void testRemoveFromActiveSleeps() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        commandsHandler.removeFromActiveSleeps(sleeps);
        verify(repo, times(5)).removeActiveSleep(any(Integer.class));
    }

    @Test
    public void testGetStoppedSleeps() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        stoppedSleepsSetup(sleeps);
        List<Sleep> sleeps2 = commandsHandler.getStoppedSleeps();
        assertEquals(sleeps, sleeps2);
    }

    @Test
    public void testGetStoppedSleepsEmptyList() {
        ArrayList<Sleep> sleeps = new ArrayList<>();
        stoppedSleepsSetup(sleeps);
        List<Sleep> sleeps2 = commandsHandler.getStoppedSleeps();
        assertEquals(sleeps, sleeps2);
        assertEquals(sleeps2.size(), 0);
    }

    @Test
    public void testStopVentilationForStoppedSleeps() {
        ArrayList<Sleep> sleeps = instantiateSleeps();

        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn("string");

        ArrayList<Command> commands = commandsHandler.stopVentilationForStoppedSleeps(sleeps);

        verify(repo, times(5)).deleteVentilationFromDb(any(String.class));
        assertEquals(commands.size(), sleeps.size());

        for (int i = 0; i < commands.size(); ++i)
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
    }

    @Test
    public void testStopVentilationForStoppedSleepsNoVentilation() {
        ArrayList<Sleep> sleeps = instantiateSleeps();

        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);

        ArrayList<Command> commands = commandsHandler.stopVentilationForStoppedSleeps(sleeps);

        verify(repo, times(0)).deleteVentilationFromDb(any(String.class));
        assertEquals(commands.size(), 0);
    }

    @Test
    public void testStopVentilationForStoppedSleepsOnlyOneVentilation() {
        ArrayList<Sleep> sleeps = instantiateSleeps();

        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);
        when(repo.getDeviceIdFromActiveVentilations("device1")).thenReturn("device1");

        ArrayList<Command> commands = commandsHandler.stopVentilationForStoppedSleeps(sleeps);

        verify(repo, times(1)).deleteVentilationFromDb(any(String.class));
        assertEquals(commands.size(), 1);
        assertEquals(commands.get(0).getDestination(), "device1");
    }

    @Test
    public void testStopVentilationForStoppedSleepsEmptyList() {
        ArrayList<Sleep> sleeps = new ArrayList<>();
        ArrayList<Command> commands = commandsHandler.stopVentilationForStoppedSleeps(sleeps);

        verify(repo, times(0)).deleteVentilationFromDb(any(String.class));

        assertEquals(commands.size(), 0);
    }

    @Test
    public void testGetStoppedSleepCommands() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        stoppedSleepsSetup(sleeps);
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn("string");

        ArrayList<Command> commands = commandsHandler.getStoppedSleepCommands();

        assertEquals(commands.size(), 10);

        for (int i = 0; i < 5; ++i) {
            assertEquals(commands.get(i).getCommandID(), 'D');
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
        }
        for (int i = 5; i < 10; ++i) {
            assertEquals(commands.get(i).getCommandID(), 'V');
            assertEquals(commands.get(i).getDestination(), sleeps.get(i - 5).getDeviceId());
        }
    }

    @Test
    public void testGetStoppedSleepCommandsNoVentilation() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        stoppedSleepsSetup(sleeps);
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);

        ArrayList<Command> commands = commandsHandler.getStoppedSleepCommands();

        assertEquals(commands.size(), 5);

        for (int i = 0; i < 5; ++i) {
            assertEquals(commands.get(i).getCommandID(), 'D');
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
        }
    }

    @Test
    public void testGetStoppedSleepCommandsOnlyOne() {
        ArrayList<Sleep> sleeps = instantiateSleeps();
        stoppedSleepsSetup(sleeps);
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);
        when(repo.getDeviceIdFromActiveVentilations("device1")).thenReturn("device1");

        ArrayList<Command> commands = commandsHandler.getStoppedSleepCommands();

        assertEquals(commands.size(), 6);

        for (int i = 0; i < 5; ++i) {
            assertEquals(commands.get(i).getCommandID(), 'D');
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
        }
        assertEquals(commands.get(5).getCommandID(), 'V');
        assertEquals(commands.get(5).getDestination(), sleeps.get(0).getDeviceId());
    }

    @Test
    public void testGetStoppedSleepCommandsNoStoppedSleeps() {
        ArrayList<Sleep> sleeps = new ArrayList<>();
        stoppedSleepsSetup(sleeps);

        ArrayList<Command> commands = commandsHandler.getStoppedSleepCommands();

        assertEquals(commands.size(), 0);
    }

    //regulations
    private void isSleepAlreadyRegulatedSetupTrueForAll()
    {
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn("string");
    }

    private void isSleepAlreadyRegulatedSetupFalseForAll()
    {
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);
    }

    private void isSleepAlreadyRegulatedSetupVarious()
    {
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);
        when(repo.getDeviceIdFromActiveVentilations("device2")).thenReturn("string");
        when(repo.getDeviceIdFromActiveVentilations("device3")).thenReturn("string");
    }

    @Test
    public void testIsSleepAlreadyRegulatedTrueForAll()
    {
        isSleepAlreadyRegulatedSetupTrueForAll();
        List<Sleep> sleeps = instantiateSleeps();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            assertTrue(commandsHandler.isSleepAlreadyRegulated(sleeps.get(i)));
        }
    }

    @Test
    public void testIsSleepAlreadyRegulatedFalseForAll()
    {
        isSleepAlreadyRegulatedSetupFalseForAll();
        List<Sleep> sleeps = instantiateSleeps();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            assertFalse(commandsHandler.isSleepAlreadyRegulated(sleeps.get(i)));
        }
    }

    @Test
    public void testIsSleepAlreadyRegulatedVarious()
    {
        isSleepAlreadyRegulatedSetupVarious();
        List<Sleep> sleeps = instantiateSleeps();

        assertFalse(commandsHandler.isSleepAlreadyRegulated(sleeps.get(0)));
        assertTrue(commandsHandler.isSleepAlreadyRegulated(sleeps.get(1)));
        assertTrue(commandsHandler.isSleepAlreadyRegulated(sleeps.get(2)));
        assertFalse(commandsHandler.isSleepAlreadyRegulated(sleeps.get(3)));
        assertFalse(commandsHandler.isSleepAlreadyRegulated(sleeps.get(4)));
    }

    @Test
    public void testGetStartVentilationCommandTrueForAll()
    {
        List<Sleep> sleeps = instantiateSleeps();
        isSleepAlreadyRegulatedSetupTrueForAll();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            assertNull(commandsHandler.getStartVentilationCommand(sleeps.get(i)));
        }
    }

    @Test
    public void testGetStartVentilationCommandFalseForAll()
    {
        List<Sleep> sleeps = instantiateSleeps();
        isSleepAlreadyRegulatedSetupFalseForAll();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            assertNotNull(commandsHandler.getStartVentilationCommand(sleeps.get(i)));
            assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(i)).getDestination(), sleeps.get(i).getDeviceId());
            assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(i)).getCommandID(), 'V');
            assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(i)).getValue(), 1);
        }
    }

    @Test
    public void testGetStartVentilationCommandVarious()
    {
        List<Sleep> sleeps = instantiateSleeps();
        isSleepAlreadyRegulatedSetupVarious();

        assertNotNull(commandsHandler.getStartVentilationCommand(sleeps.get(0)));
        assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(0)).getDestination(), sleeps.get(0).getDeviceId());
        assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(0)).getCommandID(), 'V');
        assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(0)).getValue(), 1);
        assertNull(commandsHandler.getStartVentilationCommand(sleeps.get(1)));
        assertNull(commandsHandler.getStartVentilationCommand(sleeps.get(2)));
        assertNotNull(commandsHandler.getStartVentilationCommand(sleeps.get(3)));
        assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(3)).getDestination(), sleeps.get(3).getDeviceId());
        assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(3)).getCommandID(), 'V');
        assertEquals(commandsHandler.getStartVentilationCommand(sleeps.get(3)).getValue(), 1);
    }

    @Test
    public void testGetStopVentilationCommandTrueForAll()
    {
        List<Sleep> sleeps = instantiateSleeps();
        isSleepAlreadyRegulatedSetupTrueForAll();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            assertNotNull(commandsHandler.getStopVentilationCommand(sleeps.get(i)));
            assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(i)).getDestination(), sleeps.get(i).getDeviceId());
            assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(i)).getCommandID(), 'V');
            assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(i)).getValue(), 0);
        }
    }

    @Test
    public void testGetStopVentilationCommandFalseForAll()
    {
        List<Sleep> sleeps = instantiateSleeps();
        isSleepAlreadyRegulatedSetupFalseForAll();

        for(int i = 0; i < sleeps.size(); ++i)
        {
            assertNull(commandsHandler.getStopVentilationCommand(sleeps.get(i)));
        }
    }

    @Test
    public void testGetStopVentilationCommandVarious()
    {
        List<Sleep> sleeps = instantiateSleeps();
        isSleepAlreadyRegulatedSetupVarious();

        assertNull(commandsHandler.getStopVentilationCommand(sleeps.get(0)));
        assertNotNull(commandsHandler.getStopVentilationCommand(sleeps.get(1)));
        assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(1)).getDestination(), sleeps.get(1).getDeviceId());
        assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(1)).getCommandID(), 'V');
        assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(1)).getValue(), 0);
        assertNotNull(commandsHandler.getStopVentilationCommand(sleeps.get(2)));
        assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(2)).getDestination(), sleeps.get(2).getDeviceId());
        assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(2)).getCommandID(), 'V');
        assertEquals(commandsHandler.getStopVentilationCommand(sleeps.get(2)).getValue(), 0);
        assertNull(commandsHandler.getStopVentilationCommand(sleeps.get(3)));
    }

    @Test
    public void testCreateCommandSleepWithGoodRCAndRegulated()
    {
        Sleep sleep = instantiateSleeps().get(0);
        when(repo.getDeviceIdFromActiveVentilations(sleep.getDeviceId())).thenReturn("string");
        assertNotNull(commandsHandler.createCommand(sleep, 4));
        assertEquals(commandsHandler.createCommand(sleep, 4).getDestination(), sleep.getDeviceId());
        assertEquals(commandsHandler.createCommand(sleep, 4).getCommandID(), 'V');
        assertEquals(commandsHandler.createCommand(sleep, 4).getValue(), 0);
    }

    @Test
    public void testCreateCommandSleepWithGoodRCAndNotRegulated()
    {
        Sleep sleep = instantiateSleeps().get(0);
        when(repo.getDeviceIdFromActiveVentilations(sleep.getDeviceId())).thenReturn(null);
        assertNull(commandsHandler.createCommand(sleep, 4));
    }

    @Test
    public void testCreateCommandSleepWithBadRCAndRegulated()
    {
        Sleep sleep = instantiateSleeps().get(0);
        when(repo.getDeviceIdFromActiveVentilations(sleep.getDeviceId())).thenReturn("string");
        assertNull(commandsHandler.createCommand(sleep, 2));
    }

    @Test
    public void testCreateCommandSleepWithBadRCAndNotRegulated()
    {
        Sleep sleep = instantiateSleeps().get(0);
        when(repo.getDeviceIdFromActiveVentilations(sleep.getDeviceId())).thenReturn(null);
        assertNotNull(commandsHandler.createCommand(sleep, 2));
        assertEquals(commandsHandler.createCommand(sleep, 2).getDestination(), sleep.getDeviceId());
        assertEquals(commandsHandler.createCommand(sleep, 2).getCommandID(), 'V');
        assertEquals(commandsHandler.createCommand(sleep, 2).getValue(), 1);
    }

    private void getCountOfLatestGoodValuesSetupAllGood()
    {
        when(repo.getCountOfLatestGoodValues(any(Integer.class))).thenReturn(4);
    }

    private void getCountOfLatestGoodValuesSetupAllBad()
    {
        when(repo.getCountOfLatestGoodValues(any(Integer.class))).thenReturn(2);
    }

    private void getCountOfLatestGoodValuesSetupVarious(List<Sleep> sleeps)
    {
        when(repo.getCountOfLatestGoodValues(any(Integer.class))).thenReturn(4);
        when(repo.getCountOfLatestGoodValues(sleeps.get(1).getSleepId())).thenReturn(2);
        when(repo.getCountOfLatestGoodValues(sleeps.get(2).getSleepId())).thenReturn(2);
    }

    private void getActiveSleepsWhereRegulationIsEnabledSetup(List<Sleep> sleeps)
    {
        when(repo.getActiveSleepsWhereRegulationIsEnabled()).thenReturn(sleeps);
    }

    @Test
    public void testGetVentilationCommandsAllGoodAndAlreadyRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupAllGood();
        isSleepAlreadyRegulatedSetupTrueForAll();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 5);

        for(int i = 0; i < commands.size(); ++i)
        {
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
            assertEquals(commands.get(i).getCommandID(), 'V');
            assertEquals(commands.get(i).getValue(), 0);
        }
    }

    @Test
    public void testGetVentilationCommandsAllGoodNotRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupAllGood();
        isSleepAlreadyRegulatedSetupFalseForAll();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 0);
    }

    @Test
    public void testGetVentilationCommandsAllGoodSomeRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupAllGood();
        isSleepAlreadyRegulatedSetupVarious();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 2);

        assertEquals(commands.get(0).getDestination(), sleeps.get(1).getDeviceId());
        assertEquals(commands.get(0).getCommandID(), 'V');
        assertEquals(commands.get(0).getValue(), 0);
        assertEquals(commands.get(1).getDestination(), sleeps.get(2).getDeviceId());
        assertEquals(commands.get(1).getCommandID(), 'V');
        assertEquals(commands.get(1).getValue(), 0);

    }

    @Test
    public void testGetVentilationCommandsAllBadAndAlreadyRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupAllBad();
        isSleepAlreadyRegulatedSetupTrueForAll();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 0);
    }

    @Test
    public void testGetVentilationCommandsAllBadAndNotRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupAllBad();
        isSleepAlreadyRegulatedSetupFalseForAll();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 5);

        for(int i = 0; i < commands.size(); ++i)
        {
            assertEquals(commands.get(i).getDestination(), sleeps.get(i).getDeviceId());
            assertEquals(commands.get(i).getCommandID(), 'V');
            assertEquals(commands.get(i).getValue(), 1);
        }
    }

    @Test
    public void testGetVentilationCommandsAllBadSomeRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupAllBad();
        isSleepAlreadyRegulatedSetupVarious();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 3);

        assertEquals(commands.get(0).getDestination(), sleeps.get(0).getDeviceId());
        assertEquals(commands.get(0).getCommandID(), 'V');
        assertEquals(commands.get(0).getValue(), 1);
        assertEquals(commands.get(1).getDestination(), sleeps.get(3).getDeviceId());
        assertEquals(commands.get(1).getCommandID(), 'V');
        assertEquals(commands.get(1).getValue(), 1);
        assertEquals(commands.get(2).getDestination(), sleeps.get(4).getDeviceId());
        assertEquals(commands.get(2).getCommandID(), 'V');
        assertEquals(commands.get(2).getValue(), 1);
    }

    @Test
    public void testGetVentilationCommandsSomeGoodAllRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupVarious(sleeps);
        isSleepAlreadyRegulatedSetupTrueForAll();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 3);

        assertEquals(commands.get(0).getDestination(), sleeps.get(0).getDeviceId());
        assertEquals(commands.get(0).getCommandID(), 'V');
        assertEquals(commands.get(0).getValue(), 0);
        assertEquals(commands.get(1).getDestination(), sleeps.get(3).getDeviceId());
        assertEquals(commands.get(1).getCommandID(), 'V');
        assertEquals(commands.get(1).getValue(), 0);
        assertEquals(commands.get(2).getDestination(), sleeps.get(4).getDeviceId());
        assertEquals(commands.get(2).getCommandID(), 'V');
        assertEquals(commands.get(2).getValue(), 0);
    }

    @Test
    public void testGetVentilationCommandsSomeGoodNoneRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupVarious(sleeps);
        isSleepAlreadyRegulatedSetupFalseForAll();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 2);

        assertEquals(commands.get(0).getDestination(), sleeps.get(1).getDeviceId());
        assertEquals(commands.get(0).getCommandID(), 'V');
        assertEquals(commands.get(0).getValue(), 1);
        assertEquals(commands.get(1).getDestination(), sleeps.get(2).getDeviceId());
        assertEquals(commands.get(1).getCommandID(), 'V');
        assertEquals(commands.get(1).getValue(), 1);
    }

    @Test
    public void testGetVentilationCommandsSomeGoodSameRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupVarious(sleeps);
        isSleepAlreadyRegulatedSetupVarious();

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 0);
    }

    @Test
    public void testGetVentilationCommandsSomeGoodOtherRegulated()
    {
        List<Sleep> sleeps = instantiateSleeps();
        getActiveSleepsWhereRegulationIsEnabledSetup(sleeps);
        getCountOfLatestGoodValuesSetupVarious(sleeps);
        //isSleepAlreadyRegulatedSetupVarious();
        when(repo.getDeviceIdFromActiveVentilations(any(String.class))).thenReturn(null);
        when(repo.getDeviceIdFromActiveVentilations("device1")).thenReturn("string");
        when(repo.getDeviceIdFromActiveVentilations("device3")).thenReturn("string");

        List<Command> commands = commandsHandler.getVentilationCommands();
        assertEquals(commands.size(), 2);

        assertEquals(commands.get(0).getDestination(), sleeps.get(0).getDeviceId());
        assertEquals(commands.get(0).getCommandID(), 'V');
        assertEquals(commands.get(0).getValue(), 0);
        assertEquals(commands.get(1).getDestination(), sleeps.get(1).getDeviceId());
        assertEquals(commands.get(1).getCommandID(), 'V');
        assertEquals(commands.get(1).getValue(), 1);
    }
}