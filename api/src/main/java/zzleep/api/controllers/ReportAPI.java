package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.Interval;
import zzleep.core.models.IntervalReport;
import zzleep.core.models.SleepData;
import zzleep.core.models.SleepSession;
import zzleep.core.repositories.WarehouseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/reports")
@Api(value = "Report api")
public class ReportAPI extends ControllerBase {

    private final WarehouseRepository warehouseRepository;

    public ReportAPI(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @ApiOperation(value = "Get a report on sleep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved a report"),
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity getReport(
        @PathVariable(name = "deviceId")
            String deviceId,
        @RequestParam(name = "dateStart", defaultValue = "1970-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateStart,
        @RequestParam(name = "dateFinish", defaultValue = "2100-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFinish
    ) {
        IntervalReport report = warehouseRepository.getReport(deviceId, new Interval(dateStart, dateFinish));
        return ResponseEntity
            .status(200)
            .body(report);
    }

    @ApiOperation(value = "Get data for a sleep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The sleep data has been successfully retrieved"),
        @ApiResponse(code = 404, message = NOT_FOUND_MESSAGE)
    })
    @GetMapping("/sleeps/{sleepId}")
    public ResponseEntity getSleepData(
        @PathVariable(name = "sleepId") int sleepId
    ) {
        SleepData data = warehouseRepository.getSleepData(sleepId);
        return data == null ? notFound(NOT_FOUND_MESSAGE) : success(data);
    }

}
