package schedule.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
	private List<Section> sections;

    public Schedule() {
        this.sections = new ArrayList<>();
    }

    public Schedule(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section s) {
        sections.add(s);
    }

    /** Calculate total credits */
    public int getTotalCredits() {
        int total = 0;
        for (Section s : sections) {
            total += s.getCourse().getCredits();
        }
        return total;
    }

    /** Detect time conflicts between sections */
    public boolean hasConflicts() {
        for (int i = 0; i < sections.size(); i++) {
            for (int j = i + 1; j < sections.size(); j++) {
                if (sections.get(i).conflictsWith(sections.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Basic preference checking */
    public boolean respectsPreferences(StudentPreferences p) {
        int credits = getTotalCredits();

        if (credits < p.getMinCredits() || credits > p.getMaxCredits()) {
            return false;
        }

        // Time + avoid day rules
        for (Section s : sections) {
            for (TimeSlot t : s.getTimeSlots()) {
                if (p.getAvoidDays().contains(t.getDay())) {
                    return false;
                }
                if (t.getStart().isBefore(p.getEarliestStart())) {
                    return false;
                }
                if (t.getEnd().isAfter(p.getLatestEnd())) {
                    return false;
                }
            }
        }

        return true;
    }
    public void printWeekly() {
        System.out.println("\n+------------------------------------------------------------------+");
        System.out.println("|                         WEEKLY SCHEDULE                          |");
        System.out.println("+------------------------------------------------------------------+\n");

        for (DayOfWeek day : DayOfWeek.values()) {

            if (day.getValue() > 5) continue; // skip Saturday, Sunday

            System.out.println(" " + day);
            System.out.println(" --------------------------------------------------");

            List<Section> list = getSectionsForDay(day);

            if (list.isEmpty()) {
                System.out.println("   No classes");
            } else {
                for (Section s : list) {
                    TimeSlot t = getTimeSlotForDay(s, day);
                    System.out.println("   " + t.getStart() + "–" + t.getEnd() +
                                       "   " + s.getCourse().getCode() +
                                       "    Sec " + s.getSectionId() +
                                       "  (" + s.getInstructor() + ")");
                }
            }

            System.out.println();
        }

        System.out.println("+------------------------------------------------------------------+");
    }
    private List<Section> getSectionsForDay(DayOfWeek day) {
        List<Section> list = new ArrayList<Section>();
        for (Section s : sections) {
            for (TimeSlot t : s.getTimeSlots()) {
                if (t.getDay().equals(day)) {
                    list.add(s);
                    break;
                }
            }
        }
        return list;
    }

    private TimeSlot getTimeSlotForDay(Section s, DayOfWeek day) {
        for (TimeSlot t : s.getTimeSlots()) {
            if (t.getDay().equals(day)) {
                return t;
            }
        }
        return null;
    }

}
