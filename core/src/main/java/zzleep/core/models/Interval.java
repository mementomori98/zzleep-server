package zzleep.core.models;

import java.time.LocalDate;

public class Interval {
    private final LocalDate start;
    private final LocalDate end;

    public Interval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
