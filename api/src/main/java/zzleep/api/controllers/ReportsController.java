package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;
import zzleep.core.repositories.WarehouseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/reports")
@Api(tags = {"Reports"}, description = " ")
public class ReportsController extends ControllerBase {

    private final WarehouseRepository warehouseRepository;

    public ReportsController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @ApiOperation(value = "Get a report on sleep", response = IntervalReport.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved a report"),
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
        IntervalReport report = warehouseRepository.getReport(deviceId, new Interval(dateStart, dateFinish));
        return ResponseEntity
            .status(200)
            .body(report);
    }

    @ApiOperation(value = "Get data for a sleep", response = SleepData.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The sleep data has been successfully retrieved"),
        @ApiResponse(code = 404, message = NOT_FOUND_MESSAGE)
    })
    @GetMapping("/sleeps/{sleepId}")
    public ResponseEntity<SleepData> getSleepData(
        @PathVariable(name = "sleepId") int sleepId
    ) {
        SleepData data = warehouseRepository.getSleepData(sleepId);
        return data == null ? notFound() : success(data);
    }

    @ApiOperation(value = "Get the ideal room conditions for a device (room)", response = IdealRoomConditions.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The ideal room conditions have been successfully retrieved")
    })
    @GetMapping("/ideal/{deviceId}")
    public ResponseEntity<IdealRoomConditions> getIdealRoomConditions(
        @PathVariable(value = "deviceId") String deviceId
    ) {
        return success(
            warehouseRepository.getIdealRoomCondition(deviceId)
        );
    }

}
