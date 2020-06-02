package zzleep.core.services;

import org.springframework.stereotype.Component;
import zzleep.core.models.Preferences;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.PreferencesRepository;

@Component
public class PreferencesServiceImpl extends ServiceBase implements PreferencesService {

    private final PreferencesRepository preferencesRepository;
    private final AuthorizationService authService;

    public PreferencesServiceImpl(PreferencesRepository preferencesRepository, AuthorizationService authService) {
        this.preferencesRepository = preferencesRepository;
        this.authService = authService;
    }

    @Override
    public Response<Preferences> getByDeviceId(Authorized<String> request) {
        String deviceId = request.getModel();
        if (!authService.userHasDevice(request.getUserId(), deviceId)) return unauthorized();
        Preferences preferences = preferencesRepository.getPreferences(deviceId);
        if (preferences == null) notFound();
        return success(preferences);
    }

    @Override
    public Response<Preferences> update(Authorized<Preferences> request) {
        Preferences preferences = request.getModel();
        if (!authService.userHasDevice(request.getUserId(), preferences.getDeviceId())) return unauthorized();
        try {
            Preferences returnPreferences = preferencesRepository.setPreferences(preferences);
            if (preferences == null) notFound();
            return success(returnPreferences);
        } catch (PreferencesRepository.InvalidValuesException e) {
            return notFound();
        }
    }
}
