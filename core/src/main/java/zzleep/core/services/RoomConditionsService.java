package zzleep.core.services;

import zzleep.core.models.RoomCondition;

public interface RoomConditionsService {

    Response<RoomCondition> getCurrent(Authorized<String> request);
    Response<RoomCondition> getLatest(Authorized<String> request);

}
