package zzleep.core.repositories;

public interface AuthorizationService {

    boolean userHasDevice(String userId, String deviceId);
    boolean userHasSleep(String userId, int sleepId);

}
