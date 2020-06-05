package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.GetIntervalReportModel;
import zzleep.core.models.IdealRoomConditions;
import zzleep.core.models.IntervalReport;
import zzleep.core.models.SleepData;
import zzleep.core.services.Authorized;
import zzleep.core.services.ReportService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@Api(tags = {"Reports"}, description = " ")
public class ReportsController extends ControllerBase {

    private final ReportService reportService;

    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @ApiOperation(value = "Get a report on sleep", response = IntervalReport.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved a report"),
        @ApiResponse(code = 403, message = "The user does not have a device with this ID"),
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<IntervalReport> getReport(
        @PathVariable(name = "deviceId")
            String deviceId,
        @RequestParam(name = "dateStart", defaultValue = "1970-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateStart,
        @RequestParam(name = "dateFinish", defaultValue = "2100-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFinish
    ) {
        return map(
            reportService.getReport(new Authorized<>(userId(),
                new GetIntervalReportModel(deviceId, dateStart, dateFinish)
            ))
        );
    }

    @ApiOperation(value = "Get data for a sleep", response = SleepData.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The sleep data has been successfully retrieved"),
        @ApiResponse(code = 403, message = "There is no sleep with this ID associated with this user")
    })
    @GetMapping("/sleeps/{sleepId}")
    public ResponseEntity<SleepData> getSleepData(
            @PathVariable(name = "sleepId") int sleepId
    ) {
        return map(
            reportService.getSleepData(new Authorized<>(userId(), sleepId))
        );
    }

    @ApiOperation(value = "Get the ideal room conditions for a device (room)", response = IdealRoomConditions.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The ideal room conditions have been successfully retrieved"),
        @ApiResponse(code = 403, message = "The user does not have a device with this id")
    })
    @GetMapping("/ideal/{deviceId}")
    public ResponseEntity<IdealRoomConditions> getIdealRoomConditions(
        @PathVariable(value = "deviceId") String deviceId
    ) {
        return map(
            reportService.getIdealRoomConditions(new Authorized<>(userId(), deviceId))
        );
    }

}
