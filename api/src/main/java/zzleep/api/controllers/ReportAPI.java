package zzleep.api.controllers;

import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/reports")
@Api(value = "Report api")
public class ReportAPI {

    @ApiOperation(value = "Get a report on sleep")
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
        Random random = new Random();
        int max = random.nextInt(20);
        List<SleepSession> sessions = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            sessions.add(new SleepSession(
                i,
                deviceId,
                LocalDateTime.now().minusDays(max - i),
                LocalDateTime.now().minusDays(max - i).plusHours(8),
                random.nextInt(4) + 1,
                random.nextDouble() * 200 + 490,
                random.nextDouble() * 100,
                random.nextDouble() * 70 + 20,
                random.nextDouble() * 15 + 15
            ));
        }

        IntervalReport dummy = new IntervalReport(
            sessions
        );
        return ResponseEntity
                .status(200)
                .body(dummy);
    }

    @ApiOperation(value = "Get data for a sleep")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The sleep data has bee successfully retrieved")
    })
    @GetMapping("/sleeps/{sleepId}")
    public ResponseEntity<SleepData> getSleepData(
        @PathVariable(name = "sleepId") int sleepId
    ) {
        Random random = new Random();
        LocalDateTime start = LocalDateTime.now().minusDays(random.nextInt(20) + 1);
        int dataCount = random.nextInt(100) + 50;
        LocalDateTime finish = start.plusMinutes(5 * dataCount);
        List<RoomCondition> roomConditions = new ArrayList<>();
        for (int i = 0; i < dataCount; i++) {
            roomConditions.add(new RoomCondition(
                sleepId,
                start.plusMinutes(5 * i),
                random.nextDouble() * 15 + 15,
                random.nextDouble() * 200 + 490,
                random.nextDouble() * 70 + 20,
                random.nextDouble() * 100
            ));
        }
        SleepData dummy = new SleepData(
            sleepId,
            "hahaha",
            start,
            finish,
            random.nextInt(4) + 1,
            random.nextDouble() * 200 + 490,
            random.nextDouble() * 100,
            random.nextDouble() * 70 + 20,
            random.nextDouble() * 15 + 15,
            roomConditions
        );
        return ResponseEntity
            .status(200)
            .body(dummy);
    }

}
