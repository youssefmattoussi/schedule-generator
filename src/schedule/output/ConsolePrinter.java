package schedule.output;

import java.time.DayOfWeek;
import schedule.model.*;
import java.time.LocalTime;
import java.util.*;

public class ConsolePrinter {
	private static final List<DayOfWeek> WEEK_ORDER = Arrays.asList(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    public static void printWeekly(Schedule schedule) {
        System.out.println();
        System.out.println("+------------------------------------------------------------------+");
        System.out.println("|                         WEEKLY SCHEDULE                          |");
        System.out.println("+------------------------------------------------------------------+");
        System.out.println();

        // DAY -> SECTIONS
        Map<DayOfWeek, List<Section>> dayMap = new HashMap<>();
        for (DayOfWeek d : WEEK_ORDER) {
            dayMap.put(d, new ArrayList<Section>());
        }

        // Put each section into its day bucket
        for (Section s : schedule.getSections()) {
            for (TimeSlot t : s.getTimeSlots()) {
                DayOfWeek day = t.getDay();
                if (dayMap.containsKey(day)) {
                    // Avoid duplicates
                    if (!dayMap.get(day).contains(s)) {
                        dayMap.get(day).add(s);
                    }
                }
            }
        }

        // Sort each day's classes by start time (no streams)
        for (DayOfWeek day : WEEK_ORDER) {
            List<Section> sections = dayMap.get(day);

            for (int i = 0; i < sections.size(); i++) {
                for (int j = i + 1; j < sections.size(); j++) {
                    LocalTime start1 = getStartTime(sections.get(i), day);
                    LocalTime start2 = getStartTime(sections.get(j), day);

                    if (start1.isAfter(start2)) {
                        Section temp = sections.get(i);
                        sections.set(i, sections.get(j));
                        sections.set(j, temp);
                    }
                }
            }
        }

        // Print schedule
        for (DayOfWeek day : WEEK_ORDER) {
            System.out.println(" " + day);
            System.out.println(" --------------------------------------------------");

            List<Section> sections = dayMap.get(day);

            if (sections.isEmpty()) {
                System.out.println("   (no classes)\n");
                continue;
            }

            for (Section s : sections) {
                TimeSlot t = getTimeSlotForDay(s, day);

                System.out.printf("   %s–%s   %-10s  Sec %-3s  (%s)\n",
                        t.getStart().toString(),
                        t.getEnd().toString(),
                        s.getCourse().getCode(),
                        s.getSectionId(),
                        s.getInstructor()
                );
            }

            System.out.println();
        }

        System.out.println("+------------------------------------------------------------------+");
        System.out.println();
    }

    // Helper: find the TimeSlot of this section FOR this day
    private static TimeSlot getTimeSlotForDay(Section s, DayOfWeek day) {
        for (TimeSlot t : s.getTimeSlots()) {
            if (t.getDay().equals(day)) {
                return t;
            }
        }
        return null; // shouldn't happen
    }

    // Helper: get start time for sorting
    private static LocalTime getStartTime(Section s, DayOfWeek day) {
        for (TimeSlot t : s.getTimeSlots()) {
            if (t.getDay().equals(day)) {
                return t.getStart();
            }
        }
        return LocalTime.MAX; // if nothing found
    }
}
