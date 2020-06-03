package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import zzleep.core.models.*;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.RoomConditionsRepository;


import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static zzleep.core.services.Status.*;

public class RoomConditionsServiceImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    AuthorizationService authorizationService;

    @Mock
    RoomConditionsRepository roomConditionsRepository;

    RoomConditionsServiceImpl sut;

    @Before
    public void setUp() {
        sut = new RoomConditionsServiceImpl(authorizationService, roomConditionsRepository);
    }

    @Test(expected = Exception.class)
    public void getCurrentNullRequest() {
        try {
            sut.getCurrent(null);
        } finally {
            verify(roomConditionsRepository, never()).getCurrent(any(String.class));
        }
    }

    @Test
    public void getCurrentNullModel() {
        try {
            sut.getCurrent(new Authorized<>(""));
        } finally {
            verify(roomConditionsRepository, never()).getCurrent(any(String.class));
        }
    }

    @Test
    public void getCurrentUnauthorized() {
        String userId = "user";
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<RoomCondition> result = sut.getCurrent(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(roomConditionsRepository, never()).getCurrent(any(String.class));
    }


    @Test
    public void getCurrentNotFound() {
        String userId = "user";
        String deviceId = "deviceId";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(roomConditionsRepository.getCurrent(deviceId)).thenThrow(RoomConditionsRepository.SleepNotFoundException.class);

        Response<RoomCondition> result = sut.getCurrent(new Authorized<>(userId, deviceId));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(roomConditionsRepository, times(1)).getCurrent(deviceId);
    }

    @Test
    public void getCurrentNoContent() {
        String userId = "user";
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(roomConditionsRepository.getCurrent(any())).thenReturn(new RoomCondition());
        when(roomConditionsRepository.getCurrent(deviceId)).thenThrow(new RoomConditionsRepository.NoDataException());

        Response<RoomCondition> result = sut.getCurrent(new Authorized<>(userId, deviceId));

        assertEquals(NO_CONTENT, result.getStatus());
        verify(roomConditionsRepository, times(1)).getCurrent(deviceId);
    }

    @Test
    public void getCurrentSuccess() {
        String userId = "user";
        String deviceId = "device";

        RoomCondition expected = new RoomCondition(
                1,
                LocalDateTime.of(2020, 6, 3, 10, 34),
                22, 440, 33.6, 45.8);

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(roomConditionsRepository.getCurrent(deviceId)).thenReturn(expected);

        Response<RoomCondition> result = sut.getCurrent(new Authorized<>(userId, deviceId));

        assertEquals(SUCCESS, result.getStatus());
        verify(roomConditionsRepository, times(1)).getCurrent(any(String.class));
    }
    
    @Test(expected = Exception.class)
    public void getLatestNullRequest() {
        sut.getLatest(null);
    }

    @Test
    public void getLatestUnauthorized() {
        String userId = "user";
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<RoomCondition> result = sut.getLatest(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED, result.getStatus());
        assertNull(result.getModel());
    }

    @Test
    public void getLatestNotFound() {
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(roomConditionsRepository.getLatest(any())).thenReturn(new RoomCondition());
        when(roomConditionsRepository.getLatest(deviceId)).thenThrow(new RoomConditionsRepository.SleepNotFoundException());

        Response<RoomCondition> result = sut.getLatest(new Authorized<>("", deviceId));

        assertEquals(NOT_FOUND, result.getStatus());
        assertNull(result.getModel());
    }

    @Test
    public void getLatestNoContent() {
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(roomConditionsRepository.getLatest(any())).thenReturn(new RoomCondition());
        when(roomConditionsRepository.getLatest(deviceId)).thenThrow(new RoomConditionsRepository.NoDataException());

        Response<RoomCondition> result = sut.getLatest(new Authorized<>("", deviceId));

        assertEquals(NO_CONTENT, result.getStatus());
        assertNull(result.getModel());
    }

    @Test
    public void getLatestSuccess() {
        String deviceId = "device";
        RoomCondition expected = new RoomCondition();

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(roomConditionsRepository.getLatest(any())).thenReturn(null);
        when(roomConditionsRepository.getLatest(deviceId)).thenReturn(expected);

        Response<RoomCondition> result = sut.getLatest(new Authorized<>("", deviceId));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());
    }

}