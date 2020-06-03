package zzleep.core.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import zzleep.core.models.*;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.PreferencesRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static zzleep.core.services.Status.*;

public class PreferencesServiceImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    AuthorizationService authorizationService;

    @Mock
    PreferencesRepository repository;

    PreferencesServiceImpl sut;

    @Before
    public void setUp()
    {
        sut = new PreferencesServiceImpl(repository, authorizationService);
    }

    @Test(expected = Exception.class)
    public void getByDeviceIdNullRequest() {
        try {
            sut.getByDeviceId(null);
        } finally {
            verify(repository, never()).getPreferences(any(String.class));
        }
    }

    @Test(expected = Exception.class)
    public void getByDeviceIdNullModel() {
        try {
            sut.getByDeviceId(new Authorized<>(""));
        } finally {
            verify(repository, never()).getPreferences(any(String.class));
        }
    }

    @Test
    public void getByDeviceIdUnauthorized()
    {
        String userId = "user";
        String deviceId = "device";
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<Preferences> result = sut.getByDeviceId(new Authorized<>(userId, deviceId));

        assertEquals(UNAUTHORIZED,result.getStatus());
        verify(repository, never()).getPreferences(any(String.class));


    }

    @Test
    public void getByDeviceIdNotFound()
    {
        /**
         * Result: failed
         * Reason: the method was not returning the notFound() result. 'return' was omitted
         *         in the if statement
         * Status: fixed
         */
        String userId = "user";
        String deviceId = "device";
        when(authorizationService.userHasDevice(any(),any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);

        when(repository.getPreferences(any(String.class))).thenReturn(null);
        when(repository.getPreferences(deviceId)).thenReturn(null);

        Response<Preferences> result = sut.getByDeviceId(new Authorized<>(userId, deviceId));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(repository, times(1)).getPreferences(any(String.class));
    }

    @Test
    public void getByDeviceIdSuccess()
    {
        String userId = "user";
        String deviceId = "device";
        Preferences expected = new Preferences(deviceId, true, 444,
                38.0, 42.0, 20, 22 );
        when(authorizationService.userHasDevice(any(),any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);

        when(repository.getPreferences(any(String.class))).thenReturn(null);
        when(repository.getPreferences(deviceId)).thenReturn(expected);

        Response<Preferences> result = sut.getByDeviceId(new Authorized<>(userId, deviceId));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());
        verify(repository, times(1)).getPreferences(any(String.class));

    }

    @Test(expected = Exception.class)
    public void updateNullRequest() throws PreferencesRepository.InvalidValuesException {
        try {
            sut.update(null);
        } finally {
            verify(repository, never()).setPreferences(any(Preferences.class));
        }
    }

    @Test(expected = Exception.class)
    public void updateNullModel() throws PreferencesRepository.InvalidValuesException {
        try {
            sut.update(new Authorized<>(""));
        } finally {
            verify(repository, never()).setPreferences(any(Preferences.class));
        }
    }


    @Test
    public void updateUnauthorized() throws PreferencesRepository.InvalidValuesException {
        String userId = "user";
        String deviceId = "device";
        Preferences preferences = new Preferences(deviceId, true, 444,
                38.0, 42.0, 20, 22 );
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(false);

        Response<Preferences> result = sut.update(new Authorized<>(userId, preferences));

        assertEquals(UNAUTHORIZED, result.getStatus());
        verify(repository, never()).setPreferences(any(Preferences.class));
    }

    @Test
    public void updateNotFound() throws PreferencesRepository.InvalidValuesException {

        /**
         * Result: failed
         * Reason: 1.the method was not returning the notFound() result. 'return' was omitted
         *         in the if statement.
         *         2.in the if statement the preference from the request (which is not null)
         *         was used for checking the result from the repository.
         * Status: fixed
         */
        String userId = "user";
        String deviceId = "device";
        Preferences preferences = new Preferences(deviceId, true, 444,
                38.0, 42.0, 20, 22 );
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(repository.setPreferences(any())).thenReturn(null);
        when(repository.setPreferences(preferences)).thenReturn(null);


        Response<Preferences> result = sut.update(new Authorized<>(userId, preferences));

        assertEquals(NOT_FOUND, result.getStatus());
        verify(repository, times(1)).setPreferences(any(Preferences.class));
    }

    @Test
    public void updateSuccess() throws PreferencesRepository.InvalidValuesException {
        String userId = "user";
        String deviceId = "device";
        Preferences expected = new Preferences(deviceId, true, 444,
                38.0, 42.0, 20, 22 );
        when(authorizationService.userHasDevice(any(), any())).thenReturn(true);
        when(authorizationService.userHasDevice(userId, deviceId)).thenReturn(true);
        when(repository.setPreferences(any())).thenReturn(null);
        when(repository.setPreferences(expected)).thenReturn(expected);


        Response<Preferences> result = sut.update(new Authorized<>(userId, expected));

        assertEquals(SUCCESS, result.getStatus());
        assertEquals(expected, result.getModel());
        verify(repository, times(1)).setPreferences(any(Preferences.class));
    }


}