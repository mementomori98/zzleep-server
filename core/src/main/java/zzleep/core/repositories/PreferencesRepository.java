package zzleep.core.repositories;

import zzleep.core.models.Preferences;

public interface PreferencesRepository {

    Preferences setPreferences(Preferences preferences) throws InvalidValuesException;
    Preferences getPreferences(String deviceId);
    void delete(String deviceId);

    class InvalidValuesException extends Exception{}
}
