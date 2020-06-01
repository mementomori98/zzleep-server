package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import zzleep.TestException;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.RemoveDeviceModel;
import zzleep.core.models.UpdateDeviceModel;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.DeviceRepository;
import zzleep.core.repositories.SleepRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
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
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(deviceRepository.exists(deviceId)).thenReturn(false);

        Response<Device> result = sut.add(new Authorized<>("", new AddDeviceModel(deviceId, "")));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(deviceRepository, never()).update(any(AddDeviceModel.class));
    }

    @Test
    public void addAlreadyAdded() {
        String userId = "user";
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(userId, deviceId)).thenReturn(true);

        Response<Device> result = sut.add(new Authorized<>(userId, new AddDeviceModel(deviceId, "")));

        assertEquals(NOT_ALLOWED, result.getStatus());
        verify(deviceRepository, never()).update(any(AddDeviceModel.class));
    }

    @Test
    public void addUnauthorized() {
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(deviceRepository.hasUser(any())).thenReturn(false);
        when(deviceRepository.hasUser(deviceId)).thenReturn(true);

        Response<Device> result = sut.add(new Authorized<>("", new AddDeviceModel(deviceId, "")));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(deviceRepository, never()).update(any(AddDeviceModel.class));
    }

    @Test
    public void addSuccess() {
        Device expected = new Device("id", "name", "user");
        AddDeviceModel model = new AddDeviceModel(expected.getDeviceId(), expected.getName());
        when(deviceRepository.exists(expected.getDeviceId())).thenReturn(true);
        when(deviceRepository.update(any(AddDeviceModel.class))).thenReturn(null);
        when(deviceRepository.update(model)).thenReturn(expected);

        Response<Device> result = sut.add(new Authorized<>(expected.getUserId(), model));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());
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
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(deviceRepository.exists(deviceId)).thenReturn(false);

        Response<Device> result = sut.update(new Authorized<>("", new UpdateDeviceModel(deviceId, "")));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(deviceRepository, never()).update(any(UpdateDeviceModel.class));
    }

    @Test
    public void updateUnauthorized() {
        String deviceId = "device";
        String userId = "user";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(authService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<Device> result = sut.update(new Authorized<>(userId, new UpdateDeviceModel(deviceId, "")));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(deviceRepository, never()).update(any(UpdateDeviceModel.class));
    }

    @Test
    public void updateSuccess() {
        Device expected = new Device("id", "name", "userId");
        UpdateDeviceModel model = new UpdateDeviceModel(expected.getDeviceId(), expected.getName());
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(deviceRepository.update(any(UpdateDeviceModel.class))).thenReturn(null);
        when(deviceRepository.update(model)).thenReturn(expected);

        Response<Device> result = sut.update(new Authorized<>(expected.getUserId(), model));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());

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

    @Test(expected = Exception.class)
    public void getByIdNullRequest() {
        sut.getById(null);
    }

    @Test
    public void getByIdNotFound() {
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(deviceRepository.exists(deviceId)).thenReturn(false);

        Response<Device> result = sut.getById(new Authorized<>("", deviceId));

        assertEquals(NOT_FOUND, result.getStatus());
        assertNull(result.getModel());
    }

    @Test
    public void getByIdUnauthorized() {
        String deviceId = "id";
        String userId = "user";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(authService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<Device> result = sut.getById(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED, result.getStatus());
        assertNull(result.getModel());
    }

    @Test
    public void getByIdSuccess() {
        Device expected = new Device("id", "name", "user1");
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(deviceRepository.getById(any())).thenReturn(null);
        when(deviceRepository.getById(expected.getDeviceId())).thenReturn(expected);

        Response<Device> result = sut.getById(new Authorized<>(expected.getUserId(), expected.getDeviceId()));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());
    }

    @Test(expected = Exception.class)
    public void removeNullRequest() {
        sut.remove(null);
    }

    @Test
    public void removeNotFound() {
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(deviceRepository.exists(deviceId)).thenReturn(false);

        Response<Void> result = sut.remove(new Authorized<>("", deviceId));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(deviceRepository, never()).update(any(RemoveDeviceModel.class));
    }

    @Test
    public void removeUnauthorized() {
        String deviceId = "device";
        String userId = "user";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(authService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<Void> result = sut.remove(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(deviceRepository, never()).update(any(RemoveDeviceModel.class));
    }

    @Test
    public void removeSuccess() {
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);

        Response<Void> result = sut.remove(new Authorized<>("", deviceId));

        assertEquals(SUCCESS, result.getStatus());
        verify(deviceRepository, times(1)).update(new RemoveDeviceModel(deviceId));
    }

    @Test
    public void removeStopsTracking() {
        String deviceId = "device";
        when(deviceRepository.exists(any())).thenReturn(true);
        when(authService.userHasDevice(any(), any())).thenReturn(true);
        when(sleepRepository.isTracking(any())).thenReturn(false);
        when(sleepRepository.isTracking(deviceId)).thenReturn(true);

        Response<Void> result = sut.remove(new Authorized<>("", deviceId));

        assertEquals(SUCCESS, result.getStatus());
        verify(sleepRepository, times(1)).stopTracking(deviceId);
    }

}