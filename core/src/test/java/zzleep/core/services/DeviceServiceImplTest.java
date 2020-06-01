package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.UpdateDeviceModel;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.DeviceRepository;
import zzleep.core.repositories.SleepRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static zzleep.core.services.Status.*;

public class DeviceServiceImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    DeviceRepository deviceRepository;
    @Mock
    AuthorizationService authService;
    @Mock
    SleepRepository sleepRepository;

    DeviceServiceImpl sut;

    @Before
    public void setup() {
        sut = new DeviceServiceImpl(deviceRepository, authService, sleepRepository);
    }

    @Test(expected = Exception.class)
    public void addNullRequest() {
        try {
            sut.add(null);
        } finally {
            verify(deviceRepository, never()).update(any(AddDeviceModel.class));
        }
    }

    @Test(expected = Exception.class)
    public void addNullModel() {
        try {
            sut.add(new Authorized<>(""));
        } finally {
            verify(deviceRepository, never()).update(any(AddDeviceModel.class));
        }
    }

    @Test
    public void addNotFound() {
        when(deviceRepository.getById(any())).thenReturn(null);

        Response<Device> result = sut.add(new Authorized<>("", new AddDeviceModel()));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(deviceRepository, never()).update(any(AddDeviceModel.class));
    }

    @Test
    public void addAlreadyAdded() {
        when(deviceRepository.getById(any())).thenReturn(new Device());
        when(authService.userHasDevice(any(), any())).thenReturn(true);

        Response<Device> result = sut.add(new Authorized<>("", new AddDeviceModel()));

        assertEquals(NOT_ALLOWED, result.getStatus());
        verify(deviceRepository, never()).update(any(AddDeviceModel.class));
    }

    @Test
    public void addUnauthorized() {
        when(deviceRepository.getById(any())).thenReturn(new Device());
        when(authService.userHasDevice("", "")).thenReturn(false);
        when(deviceRepository.hasUser(any())).thenReturn(true);

        Response<Device> result = sut.add(new Authorized<>("", new AddDeviceModel()));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(deviceRepository, never()).update(any(AddDeviceModel.class));
    }

    @Test
    public void addSuccess() {
        Device expected = new Device("id", "name", "user");
        AddDeviceModel model = new AddDeviceModel(expected.getDeviceId(), expected.getName());
        when(deviceRepository.getById(any())).thenReturn(expected);
        when(authService.userHasDevice(any(), any())).thenReturn(false);
        when(deviceRepository.hasUser(any())).thenReturn(false);
        when(deviceRepository.update(model)).thenReturn(expected);

        Response<Device> result = sut.add(new Authorized<>(expected.getUserId(), model));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected.getDeviceId(), result.getModel().getDeviceId());
        assertEquals(expected.getUserId(), result.getModel().getUserId());
        assertEquals(expected.getName(), result.getModel().getName());
        assertEquals(expected.getUserId(), model.getUserId());

        verify(deviceRepository, times(1)).update(any(AddDeviceModel.class));
    }

    @Test(expected = Exception.class)
    public void updateNullRequest() {
        try {
            sut.update(null);
        } finally {
            verify(deviceRepository, never()).update(any(UpdateDeviceModel.class));
        }
    }

    @Test(expected = Exception.class)
    public void updateNullModel() {
        try {
            sut.update(new Authorized<>("", null));
        } finally {
            verify(deviceRepository, never()).update(any(UpdateDeviceModel.class));
        }
    }

    @Test
    public void updateNotFound() {
        when(deviceRepository.exists(any())).thenReturn(false);

        Response<Device> result = sut.update(new Authorized<>("", new UpdateDeviceModel()));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(deviceRepository, never()).update(any(UpdateDeviceModel.class));
    }

    @Test
    public void updateUnauthorized() {
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(false);

        Response<Device> result = sut.update(new Authorized<>("", new UpdateDeviceModel()));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(deviceRepository, never()).update(any(UpdateDeviceModel.class));
    }

    @Test
    public void updateSuccess() {
        Device expected = new Device("id", "name", "userId");
        UpdateDeviceModel model = new UpdateDeviceModel(expected.getDeviceId(), expected.getName());
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(deviceRepository.update(model)).thenReturn(expected);

        Response<Device> result = sut.update(new Authorized<>(expected.getUserId(), model));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected.getDeviceId(), result.getModel().getDeviceId());
        assertEquals(expected.getName(), result.getModel().getName());
        assertEquals(expected.getUserId(), result.getModel().getUserId());

        verify(deviceRepository, times(1)).update(any(UpdateDeviceModel.class));
    }

    @Test(expected = Exception.class)
    public void getAllByUserNullRequest() {
        sut.getAllByUser(null);
    }

    @Test
    public void getAllByUserSuccess() {
        String userId = "user";
        when(deviceRepository.getAllByUserId(userId)).thenReturn(Arrays.asList(
            new Device("", "name", ""),
            new Device("", "name2", "")
        ));

        Response<List<Device>> result = sut.getAllByUser(new Authorized<>(userId));

        assertEquals(SUCCESS, result.getStatus());
        verify(deviceRepository, times(1)).getAllByUserId(userId);
    }

    @Test
    public void getAllByUserSorted() {
        String userId = "user";
        List<Device> unsorted = Arrays.asList(
            new Device("", "name2", ""),
            new Device("", "name1", "")
        );
        List<Device> sorted = Arrays.asList(
            unsorted.get(1),
            unsorted.get(0)
        );
        when(deviceRepository.getAllByUserId(userId)).thenReturn(unsorted);

        Response<List<Device>> result = sut.getAllByUser(new Authorized<>(userId));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(sorted, result.getModel());
        verify(deviceRepository, times(1)).getAllByUserId(userId);
    }

    

}