package zzleep.core;

import zzleep.core.models.SleepSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Shit {

    void test() {
        List<SleepSession> sessions = new ArrayList<>();
        List<SleepSession> res = sessions.stream()
            .map(s -> s.getTimeStart().toLocalDate()).distinct() // Get all LocalDates
            .map(d -> sessions.stream()
                .filter(s -> s.getTimeStart().toLocalDate().equals(d))) // Map sessions to dates (Stream<Stream< SleepSession >>)
            .map(s -> new SleepSession(
                -1, "",
                s.collect(Collectors.toList()).get(0).getTimeStart().toLocalDate().atStartOfDay(),
                null, 0,
                s.mapToDouble(SleepSession::getAverageCo2).average().getAsDouble(),
                s.mapToDouble(SleepSession::getAverageHumidity).average().getAsDouble(),
                s.mapToDouble(SleepSession::getAverageTemperature).average().getAsDouble(),
                s.mapToDouble(SleepSession::getAverageSound).average().getAsDouble()
            )) // Map
        .collect(Collectors.toList());
    }

}
