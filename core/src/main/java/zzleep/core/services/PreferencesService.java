package zzleep.core.services;

import zzleep.core.models.Preferences;

public interface PreferencesService {
    Response<Preferences> getByDeviceId(Authorized<String> request);
    Response<Preferences> update(Authorized<Preferences> request);
}
