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
    RoomConditionsRepository repository;

    RoomConditionsServiceImpl sut;

    @Before
    public void setUp() {
        sut = new RoomConditionsServiceImpl(authorizationService, repository);
    }

    @Test(expected = Exception.class)
    public void getReportNullRequest() throws RoomConditionsRepository.SleepNotFoundException, RoomConditionsRepository.NoDataException {
        try {
            sut.getReport(null);
        } finally {
            verify(repository, never()).getCurrentData(any(String.class));
        }
    }

    @Test
    public void getReportNullModel() throws RoomConditionsRepository.SleepNotFoundException, RoomConditionsRepository.NoDataException {
        try {
            sut.getReport(new Authorized<>(""));
        } finally {
            verify(repository, never()).getCurrentData(any(String.class));
        }
    }

    @Test
    public void getReportUnauthorized() throws RoomConditionsRepository.SleepNotFoundException, RoomConditionsRepository.NoDataException {
        String userId = "user";
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<RoomCondition> result = sut.getReport(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED, result.getStatus());

        verify(repository, never()).getCurrentData(any(String.class));
    }


    @Test
    public void getReportNotFound() throws RoomConditionsRepository.SleepNotFoundException, RoomConditionsRepository.NoDataException {
        String userId = "user";
        String deviceId = "deviceId";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(repository.getCurrentData(deviceId)).thenThrow(RoomConditionsRepository.SleepNotFoundException.class);

        Response<RoomCondition> result = sut.getReport(new Authorized<>(userId, deviceId));

        assertEquals(NOT_FOUND, result.getStatus());

        verify(repository, times(1)).getCurrentData(deviceId);
    }

    @Test
    public void getReportNoContent() throws RoomConditionsRepository.SleepNotFoundException, RoomConditionsRepository.NoDataException {
        String userId = "user";
        String deviceId = "device";

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(repository.getCurrentData(deviceId)).thenThrow(RoomConditionsRepository.NoDataException.class);

        Response<RoomCondition> result = sut.getReport(new Authorized<>(userId, deviceId));

        assertEquals(NO_CONTENT, result.getStatus());

        verify(repository, times(1)).getCurrentData(deviceId);


    }


    @Test
    public void getReportSuccess() throws RoomConditionsRepository.SleepNotFoundException, RoomConditionsRepository.NoDataException {
        String userId = "user";
        String deviceId = "device";

        RoomCondition expected = new RoomCondition(
                1,
                LocalDateTime.of(2020, 6, 3, 10, 34),
                22, 440, 33.6, 45.8);

        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(repository.getCurrentData(deviceId)).thenReturn(expected);

        Response<RoomCondition> result = sut.getReport(new Authorized<>(userId, deviceId));


        assertEquals(SUCCESS, result.getStatus());

        verify(repository, times(1)).getCurrentData(any(String.class));


    }
}