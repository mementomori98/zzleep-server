package zzleep.core.repositories;

import org.springframework.stereotype.Component;

public interface AuthorizationService {

    boolean userHasDevice(String userId, String deviceId);
    boolean userHasSleep(String userId, int sleepId);

}
