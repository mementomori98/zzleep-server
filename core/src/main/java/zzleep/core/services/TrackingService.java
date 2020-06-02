package zzleep.core.services;

import zzleep.core.models.Sleep;
import zzleep.core.models.SleepRating;

public interface TrackingService {

    Response<Sleep> startTracking(Authorized<String> request);
    Response<Sleep> stopTracking(Authorized<String> request);
    Response<Sleep> setRating(Authorized<SleepRating> request);
    Response<Boolean> isTracking(Authorized<String> request);
}
