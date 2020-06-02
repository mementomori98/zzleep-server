package zzleep.core.services;

import zzleep.core.models.GetIntervalReportModel;
import zzleep.core.models.IdealRoomConditions;
import zzleep.core.models.IntervalReport;
import zzleep.core.models.SleepData;

public interface ReportService {

    Response<IntervalReport> getReport(Authorized<GetIntervalReportModel> request);
    Response<SleepData> getSleepData(Authorized<Integer> request);
    Response<IdealRoomConditions> getIdealRoomConditions(Authorized<String> request);


}
