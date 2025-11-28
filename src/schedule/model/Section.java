package schedule.model;

import java.util.ArrayList;
import java.util.List;

public class Section {
	private String sectionId;
    private Course course;
    private String instructor;
    private List<TimeSlot> timeSlots;

    public Section(String sectionId, Course course, String instructor) {
        this.sectionId = sectionId;
        this.course = course;
        this.instructor = instructor;
        this.timeSlots = new ArrayList<>();
    }

    public String getSectionId() {
        return sectionId;
    }

    public Course getCourse() {
        return course;
    }

    public String getInstructor() {
        return instructor;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void addTimeSlot(TimeSlot slot) {
        timeSlots.add(slot);
    }

    /** Checks if this section conflicts with another section */
    public boolean conflictsWith(Section other) {
        for (TimeSlot t1 : timeSlots) {
            for (TimeSlot t2 : other.timeSlots) {
                if (t1.conflictsWith(t2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
