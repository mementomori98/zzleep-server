package zzleep.core.services;

import org.springframework.stereotype.Component;
import zzleep.core.models.Sleep;
import zzleep.core.models.SleepRating;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.SleepRepository;

@Component
public class TrackingServiceImpl  extends ServiceBase implements TrackingService{

    private final SleepRepository repository;
    private final AuthorizationService authService;

    public TrackingServiceImpl(SleepRepository repository, AuthorizationService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Override
    public Response<Sleep> startTracking(Authorized<String> request) {

        if(!authService.userHasDevice(request.getUserId(), request.getModel())) return unauthorized();
        try{
            return success(repository.startTracking(request.getModel()));
        } catch (SleepRepository.SleepNotStoppedException ex) {
            return conflict();
        } catch (SleepRepository.DeviceNotFoundException ex) {
            return notFound();
        }
    }

    @Override
    public Response<Sleep> stopTracking(Authorized<String> request) {

        if(!authService.userHasDevice(request.getUserId(), request.getModel())) return unauthorized();

        try {
            return success(repository.stopTracking(request.getModel()));
        } catch (SleepRepository.DeviceNotFoundException ex) {
            return notFound();
        } catch (SleepRepository.SleepNotStartedException ex) {
            return conflict();
        }

    }

    @Override
    public Response<Sleep> setRating(Authorized<SleepRating> request) {

        if(!authService.userHasSleep(request.getUserId(), request.getModel().getSleepId())) return unauthorized();
        try {
            Sleep sleep = repository.rateSleep(request.getModel().getSleepId(), request.getModel().getRating());
            return success(sleep);
        } catch (SleepRepository.SleepNotFoundException ex) {
            return notFound();
        }
    }

    @Override
    public Response<Boolean> isTracking(Authorized<String> request) {
        if(!authService.userHasDevice(request.getUserId(), request.getModel())) return unauthorized();

        try {
            return success(repository.isTracking(request.getModel()));
        } catch (SleepRepository.DeviceNotFoundException e) {
            return notFound();
        }
    }
}
