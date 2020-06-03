package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import zzleep.core.models.*;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.WarehouseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static zzleep.core.services.Status.*;

public class ReportServiceImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    WarehouseRepository warehouseRepository;
    @Mock
    AuthorizationService authorizationService;

    ReportServiceImpl sut;

    @Before
    public void setUp()
    {
        sut = new ReportServiceImpl(warehouseRepository, authorizationService);
    }

    @Test (expected = Exception.class)
    public void getReportNullRequest()
    {
        try
        {
            sut.getReport(null);
        }
        finally {
            verify(warehouseRepository, never()).getReport(any(String.class), any(Interval.class));
        }
    }

    @Test (expected = Exception.class)
    public void getReportNullModel()
    {
        try
        {
            sut.getReport(new Authorized<>(""));
        }
        finally {
            verify(warehouseRepository, never()).getReport(any(String.class), any(Interval.class));
        }
    }



    @Test
    public void getReportUnauthorized()
    {
        String deviceId = "device";
        String userId = "user";
        LocalDate start = LocalDate.of(2020, 6, 2);
        LocalDate end = LocalDate.of(2020, 6, 3);
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<IntervalReport> result = sut.getReport(new Authorized<>(userId, new GetIntervalReportModel(deviceId, start, end)));

       assertEquals(UNAUTHORIZED, result.getStatus());
       verify(warehouseRepository, never()).getReport(any(String.class), any(Interval.class));


    }

    @Test
    public void getReportSuccess()
    {

        String deviceId = "device";
        String userId = "user";
        LocalDate start = LocalDate.of(2020, 6, 2);
        LocalDate end = LocalDate.of(2020, 6, 3);
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);


        Response<IntervalReport> result = sut.getReport(new Authorized<>(userId, new GetIntervalReportModel(deviceId, start, end)));

        assertEquals(SUCCESS, result.getStatus());
        verify(warehouseRepository, times(1)).getReport(any(String.class), any(Interval.class));


    }

    @Test (expected = Exception.class)
    public void getSleepDataNullRequest()
    {
        try
        {
            sut.getSleepData(null);
        }
        finally {
            verify(warehouseRepository, never()).getSleepData(any(Integer.class));
        }
    }

    @Test (expected = Exception.class)
    public void getSleepDataNullModel()
    {
        try
        {
            sut.getSleepData(new Authorized<>(""));
        }
        finally {
            verify(warehouseRepository, never()).getSleepData(any(Integer.class));
        }
    }



    @Test
    public void getSleepDataUnauthorized()
    {
        String userId = "user";
        Integer sleepId = 1;

        // TODO: 6/3/2020 Ask why it doesn't work with any() without parameters
        when(authorizationService.userHasSleep(any(String.class),any(Integer.class))).thenReturn(true);
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(false);

        Response<SleepData> result = sut.getSleepData(new Authorized<>(userId, sleepId));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(warehouseRepository, never()).getSleepData(any(Integer.class));

    }

    @Test
    public void getSleepDataNotFound()
    {

        String userId = "user";
        Integer sleepId = 1;

        when(authorizationService.userHasSleep(any(String.class),any(Integer.class))).thenReturn(true);
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(true);
        when(warehouseRepository.getSleepData(any(Integer.class))).thenReturn(null);

        Response<SleepData> result = sut.getSleepData(new Authorized<>(userId,sleepId));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(warehouseRepository, times(1)).getSleepData(any(Integer.class));
    }


    @Test
    public void getSleepDataSuccess()
    {
        String userId = "user";
        Integer sleepId = 1;

        SleepData expected = new SleepData(sleepId,"device",
                                LocalDateTime.of(2020, 6,2, 14, 33 ),
                                LocalDateTime.of(2020, 6,3, 14, 33 ),
                                3,
                                new ArrayList<RoomCondition>());

        when(authorizationService.userHasSleep(any(String.class), any(Integer.class))).thenReturn(true);
        when(authorizationService.userHasSleep(userId, sleepId)).thenReturn(true);
        when(warehouseRepository.getSleepData(any(Integer.class))).thenReturn(null);
        when(warehouseRepository.getSleepData(sleepId)).thenReturn(expected);

        Response<SleepData> result = sut.getSleepData(new Authorized<>(userId, sleepId));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());
        assertEquals(expected.getSleepId(), (int)sleepId);

        verify(warehouseRepository, times(1)).getSleepData(any(Integer.class));



    }



    @Test (expected = Exception.class)
    public void getIdealConditionsNullRequest()
    {
        try
        {
            sut.getIdealRoomConditions(null);
        }
        finally {
            verify(warehouseRepository, never()).getIdealRoomCondition(any(String.class));
        }
    }

    @Test
    public void getIdealConditionsNullModel()
    {
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice("user", null)).thenReturn(false);

        Response<IdealRoomConditions>  result = sut.getIdealRoomConditions(new Authorized<>("user"));

        assertEquals(UNAUTHORIZED, result.getStatus());
        assertNull(null, result.getModel());
        verify(warehouseRepository, never()).getIdealRoomCondition(any(String.class));


    }

    @Test
    public void getIdealRoomConditionsUnauthorized()
    {
        String userId = "user";
        String deviceId = "device";
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);


        Response<IdealRoomConditions> result = sut.getIdealRoomConditions(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED, result.getStatus());
        assertNull(null, result.getModel());

        verify(warehouseRepository, never()).getIdealRoomCondition(any(String.class));

    }


    @Test
    public void getIdealConditionsSuccess()
    {
        String userId = "user";
        String deviceId = "device";

        IdealRoomConditions expected = new IdealRoomConditions(450.7, 33.6, 45.2, 22.3);
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(warehouseRepository.getIdealRoomCondition(deviceId)).thenReturn(expected);

        Response<IdealRoomConditions> result = sut.getIdealRoomConditions(new Authorized<>(userId, deviceId));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());

        verify(warehouseRepository, times(1)).getIdealRoomCondition(any(String.class));

    }
}