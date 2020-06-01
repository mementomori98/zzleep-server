package zzleep.core.services;

import org.springframework.stereotype.Component;
import zzleep.core.models.*;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.WarehouseRepository;

@Component
public class ReportServiceImpl extends ServiceBase implements ReportService{

    private final WarehouseRepository warehouseRepository;
    private final AuthorizationService authorizationService;

    public ReportServiceImpl(WarehouseRepository warehouseRepository, AuthorizationService authorizationService) {
        this.warehouseRepository = warehouseRepository;
        this.authorizationService = authorizationService;
    }



    @Override
    public Response<IntervalReport> getReport(Authorized<GetIntervalReportModel> request) {

        GetIntervalReportModel model = request.getModel();
        IntervalReport report = warehouseRepository.getReport(model.getDeviceId(), new Interval(model.getStart(),model.getEnd()));

        if (!authorizationService.userHasDevice(request.getUserId(), model.getDeviceId())) return unauthorized();

        return success(report);
    }

    @Override
    public Response<SleepData> getSleepData(Authorized<Integer> request) {

        String userId = request.getUserId();
        Integer sleepId = request.getModel();
        if(!authorizationService.userHasSleep(userId,sleepId)) return unauthorized();

        SleepData data = warehouseRepository.getSleepData(request.getModel());

        return data == null ? notFound():success(data);
    }

    @Override
    public Response<IdealRoomConditions> getIdealRoomConditions(Authorized<String> request) {

        if(!authorizationService.userHasDevice(request.getUserId(), request.getModel())) return unauthorized();

        return success(warehouseRepository.getIdealRoomCondition(request.getModel()));
    }
}
