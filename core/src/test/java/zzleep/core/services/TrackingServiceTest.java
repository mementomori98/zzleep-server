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
    SleepRepository sleepRepository;

    TrackingService sut;

    @Before
    public void setUp()
    {
        sut = new TrackingServiceImpl(sleepRepository,authorizationService);
    }

    private void setupGoodDeviceAuth(String userId, String deviceId)
    {
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
    }

    private void setupGoodSleepAuth(String userId, int sleepId)
    {
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(true);
    }

    private void setupBadSleepAuth(String userId, int sleepId)
    {
        when(authorizationService.userHasSleep(any(), anyInt())).thenReturn(true);
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(false);
    }

    private void setupBadDeviceAuth(String userId, String deviceId)
    {
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);
    }

    @Test
    public void testStartTrackingBadAuth()
    {
        setupBadDeviceAuth("user1", "device1");
        Response<Sleep> response = sut.startTracking(
            new Authorized<>("user1", "device1")
        );
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(sleepRepository, never()).startTracking(any(String.class));
    }

    @Test
    public void testStartTrackingGoodAuth()
    {
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.startTracking("device1"))
            .thenReturn(new Sleep(1, "device1", LocalDateTime.now(), null, 0));

        Response<Sleep> response = sut.startTracking(new Authorized<>("user1", "device1"));

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
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.startTracking("device1"))
            .thenThrow(new SleepRepository.SleepNotStoppedException());

        Response<Sleep> response = sut.startTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.CONFLICT);
        assertNull(response.getModel());
    }

    @Test
    public void testStartTrackingDeviceNotFound()
    {
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.startTracking("device1"))
            .thenThrow(new SleepRepository.DeviceNotFoundException());

        Response<Sleep> response = sut.startTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
    }

    @Test
    public void testStopTrackingBadAuth()
    {
        setupBadDeviceAuth("user1", "device1");
        Response<Sleep> response = sut.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(sleepRepository, never()).startTracking(any(String.class));
    }

    @Test
    public void testStopTrackingGoodAuth()
    {
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.stopTracking("device1"))
            .thenReturn(new Sleep(1, "device1", LocalDateTime.now(), LocalDateTime.now(), 0));

        Response<Sleep> response = sut.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response.getModel().getDeviceId(), "device1");
        assertEquals(response.getModel().getSleepId(), 1);
        assertNotNull(response.getModel().getDateTimeStart());
        assertNotNull(response.getModel().getDateTimeFinish());
        assertEquals(response.getModel().getRating(), 0);
    }

    @Test
    public void testStopTrackingSleepNotStarted()
    {
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.stopTracking("device1"))
            .thenThrow(new SleepRepository.SleepNotStartedException());

        Response<Sleep> response = sut.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.CONFLICT);
        assertNull(response.getModel());
    }

    @Test
    public void testStopTrackingDeviceNotFound()
    {
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.stopTracking("device1"))
            .thenThrow(new SleepRepository.DeviceNotFoundException());

        Response<Sleep> response = sut.stopTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
        assertNull(response.getModel());
    }

    @Test
    public void testSetRatingBadAuth()
    {
        setupBadSleepAuth("user1", 1);
        SleepRating sleepRating = new SleepRating(1, 4);
        Response<Sleep> response = sut.setRating(new Authorized<>("user1", sleepRating));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(sleepRepository, never()).startTracking(any(String.class));
    }

    @Test
    public void testSetRatingGoodAuth()
    {
        setupGoodSleepAuth("user1", 1);
        SleepRating sleepRating = new SleepRating(1, 4);
        when(sleepRepository.rateSleep(any(Integer.class), any(Integer.class)))
            .thenReturn(new Sleep(1, "device1", LocalDateTime.now(), LocalDateTime.now(), 4));

        Response<Sleep> response = sut.setRating(new Authorized<>("user1", sleepRating));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response.getModel().getDeviceId(), "device1");
        assertEquals(response.getModel().getSleepId(), 1);
        assertNotNull(response.getModel().getDateTimeStart());
        assertNotNull(response.getModel().getDateTimeFinish());
        assertEquals(response.getModel().getRating(), 4);
    }

    @Test
    public void testSetRatingSleepNotFound()
    {
        setupGoodSleepAuth("user1", 1);
        SleepRating sleepRating = new SleepRating(1, 4);
        when(sleepRepository.rateSleep(any(Integer.class), any(Integer.class)))
            .thenThrow(new SleepRepository.SleepNotFoundException());

        Response<Sleep> response = sut.setRating(new Authorized<>("user1", sleepRating));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
        assertNull(response.getModel());
    }

    @Test
    public void testIsTrackingBadAuth()
    {
        setupBadDeviceAuth("user1", "device1");
        Response<Boolean> response = sut.isTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.UNAUTHORIZED);
        verify(sleepRepository, never()).isTracking(any(String.class));
    }

    @Test
    public void testIsTrackingGoodAuth()
    {
        setupGoodDeviceAuth("user1", "device1");
        setupGoodDeviceAuth("user1", "device2");
        when(sleepRepository.isTracking("device1")).thenReturn(true);
        when(sleepRepository.isTracking("device2")).thenReturn(false);

        Response<Boolean> response = sut.isTracking(new Authorized<>("user1", "device1"));
        Response<Boolean> response2 = sut.isTracking(new Authorized<>("user1", "device2"));
        assertEquals(response.getStatus(), Status.SUCCESS);
        assertEquals(response2.getStatus(), Status.SUCCESS);
        assertTrue(response.getModel());
        assertFalse(response2.getModel());
    }

    @Test
    public void testIsTrackingGoodAuthDeviceNotFound()
    {
        setupGoodDeviceAuth("user1", "device1");
        when(sleepRepository.isTracking("device1")).thenThrow(new SleepRepository.DeviceNotFoundException());

        Response<Boolean> response = sut.isTracking(new Authorized<>("user1", "device1"));
        assertEquals(response.getStatus(), Status.NOT_FOUND);
        assertNull(response.getModel());
    }
}
