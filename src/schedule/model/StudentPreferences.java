package schedule.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class StudentPreferences {
	private int minCredits;
    private int maxCredits;
    private LocalTime earliestStart;
    private LocalTime latestEnd;
    private Set<DayOfWeek> avoidDays;

    public StudentPreferences(int minCredits, int maxCredits,
                              LocalTime earliestStart, LocalTime latestEnd) {
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
        this.earliestStart = earliestStart;
        this.latestEnd = latestEnd;
        this.avoidDays = new HashSet<>();
    }
    public StudentPreferences() {
        this.minCredits = 0;
        this.maxCredits = 99;
        this.avoidDays = new HashSet<DayOfWeek>();
        this.earliestStart = LocalTime.of(0, 0);
        this.latestEnd = LocalTime.of(23, 59);
    }


    public int getMinCredits() {
        return minCredits;
    }

    public int getMaxCredits() {
        return maxCredits;
    }

    public LocalTime getEarliestStart() {
        return earliestStart;
    }

    public LocalTime getLatestEnd() {
        return latestEnd;
    }

    public Set<DayOfWeek> getAvoidDays() {
        return avoidDays;
    }

    public void addAvoidDay(DayOfWeek day) {
        avoidDays.add(day);
    }
}
