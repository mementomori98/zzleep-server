package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import zzleep.core.models.Sleep;
import zzleep.core.models.SleepRating;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.SleepRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrackingServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    AuthorizationService authorizationService;

    @Mock
    SleepRepository repo;

    TrackingService service;

    @Before
    public void setUp()
    {
        service = new TrackingServiceImpl(repo,authorizationService);
    }

    private void goodAuthDeviceSetup(String userId, String deviceId)
    {
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
    }

    private void badAuthDeviceSetup(String userId, String deviceId)
    {
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);
    }

    private void goodAuthSleepSetup(String userId, int sleepId)
    {
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(true);
    }

    private void badAuthSleepSetup(String userId, int sleepId)
    {
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(false);
    }

    @Test
    public void testStartTrackingBadAuth()
    {
        badAuthDeviceSetup("user1", "device1");
        Response<Sleep> response = service.startTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(repo, never()).startTracking(any(String.class));
    }

    @Test
    public void testStartTrackingGoodAuth()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.startTracking("device1")).thenReturn(new Sleep(1, "device1", LocalDateTime.now(), null, 0));

        Response<Sleep> response = service.startTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response.getModel().getDeviceId(), "device1");
        assertEquals(response.getModel().getSleepId(), 1);
        assertNotNull(response.getModel().getDateTimeStart());
        assertNull(response.getModel().getDateTimeFinish());
        assertEquals(response.getModel().getRating(), 0);
    }

    @Test
    public void testStartTrackingGoodAuthSleepNotStopped()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.startTracking("device1")).thenThrow(new SleepRepository.SleepNotStoppedException());

        Response<Sleep> response = service.startTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.CONFLICT);
        assertNull(response.getModel());
    }

    @Test
    public void testStartTrackingGoodAuthDeviceNotFound()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.startTracking("device1")).thenThrow(new SleepRepository.DeviceNotFoundException());

        Response<Sleep> response = service.startTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
    }

    @Test
    public void testStopTrackingBadAuth()
    {
        badAuthDeviceSetup("user1", "device1");
        Response<Sleep> response = service.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(repo, never()).startTracking(any(String.class));
    }

    @Test
    public void testStopTrackingGoodAuth()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.stopTracking("device1")).thenReturn(new Sleep(1, "device1", LocalDateTime.now(), LocalDateTime.now(), 0));

        Response<Sleep> response = service.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response.getModel().getDeviceId(), "device1");
        assertEquals(response.getModel().getSleepId(), 1);
        assertNotNull(response.getModel().getDateTimeStart());
        assertNotNull(response.getModel().getDateTimeFinish());
        assertEquals(response.getModel().getRating(), 0);
    }

    @Test
    public void testStopTrackingGoodAuthSleepNotStarted()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.stopTracking("device1")).thenThrow(new SleepRepository.SleepNotStartedException());

        Response<Sleep> response = service.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.CONFLICT);
        assertNull(response.getModel());
    }

    @Test
    public void testStopTrackingGoodAuthDeviceNotFound()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.stopTracking("device1")).thenThrow(new SleepRepository.DeviceNotFoundException());

        Response<Sleep> response = service.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
        assertNull(response.getModel());
    }

    @Test
    public void testSetRatingBadAuth()
    {
        badAuthSleepSetup("user1", 1);
        SleepRating sleepRating = new SleepRating(1, 4);
        Response<Sleep> response = service.setRating(new Authorized<>("user1", sleepRating));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(repo, never()).startTracking(any(String.class));
    }

    @Test
    public void testSetRatingSetGoodAuth()
    {
        goodAuthSleepSetup("user1", 1);
        SleepRating sleepRating = new SleepRating(1, 4);
        when(repo.rateSleep(any(Integer.class), any(Integer.class))).thenReturn(new Sleep(1, "device1", LocalDateTime.now(), LocalDateTime.now(), 4));

        Response<Sleep> response = service.setRating(new Authorized<>("user1", sleepRating));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response.getModel().getDeviceId(), "device1");
        assertEquals(response.getModel().getSleepId(), 1);
        assertNotNull(response.getModel().getDateTimeStart());
        assertNotNull(response.getModel().getDateTimeFinish());
        assertEquals(response.getModel().getRating(), 4);
    }

    @Test
    public void testSetRatingSetGoodAuthSleepNotFound()
    {
        goodAuthSleepSetup("user1", 1);
        SleepRating sleepRating = new SleepRating(1, 4);
        when(repo.rateSleep(any(Integer.class), any(Integer.class))).thenThrow(new SleepRepository.SleepNotFoundException());

        Response<Sleep> response = service.setRating(new Authorized<>("user1", sleepRating));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
        assertNull(response.getModel());
    }

    @Test
    public void testIsTrackingBadAuth()
    {
        badAuthDeviceSetup("user1", "device1");
        Response<Boolean> response = service.isTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(repo, never()).isTracking(any(String.class));
    }

    @Test
    public void testIsTrackingGoodAuth()
    {
        goodAuthDeviceSetup("user1", "device1");
        goodAuthDeviceSetup("user1", "device2");
        when(repo.isTracking("device1")).thenReturn(true);
        when(repo.isTracking("device2")).thenReturn(false);

        Response<Boolean> response = service.isTracking(new Authorized<>("user1", "device1"));
        Response<Boolean> response2 = service.isTracking(new Authorized<>("user1", "device2"));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response2.getStatus(), Status.SUCCESS);
        assertTrue(response.getModel());
        assertFalse(response2.getModel());
    }

    @Test
    public void testIsTrackingGoodAuthDeviceNotFound()
    {
        goodAuthDeviceSetup("user1", "device1");
        when(repo.isTracking("device1")).thenThrow(new SleepRepository.DeviceNotFoundException());

        Response<Boolean> response = service.isTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
        assertNull(response.getModel());
    }
}
