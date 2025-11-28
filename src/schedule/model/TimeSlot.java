package schedule.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeSlot {
	private DayOfWeek day;
    private LocalTime start;
    private LocalTime end;

    public TimeSlot(DayOfWeek day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    /** Checks if two timeslots overlap on the same day */
    public boolean conflictsWith(TimeSlot other) {
        if (!this.day.equals(other.day)) {
            return false;
        }
        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }
}
