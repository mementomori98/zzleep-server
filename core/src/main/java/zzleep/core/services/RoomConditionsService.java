package zzleep.core.services;

import zzleep.core.models.RoomCondition;

public interface RoomConditionsService {

    Response<RoomCondition> getReport(Authorized<String> request);

}
