package zzleep.core.repositories;

import zzleep.core.models.Preferences;

public interface PreferencesRepository {
    Preferences setPreferences(Preferences preferences);
    Preferences getPreferences(String deviceId);
}
