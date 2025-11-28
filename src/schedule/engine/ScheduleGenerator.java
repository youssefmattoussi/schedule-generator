package schedule.engine;

import java.util.ArrayList;
import java.util.List;

import schedule.model.Course;
import schedule.model.Schedule;
import schedule.model.Section;
import schedule.model.StudentPreferences;

public class ScheduleGenerator {
	private List<Course> courses;
    private StudentPreferences preferences;
    public ScheduleGenerator(List<Course> courses, StudentPreferences preferences) {
        this.courses = courses;
        this.preferences = preferences;
    }
    
    public ScheduleGenerator() {
        this.courses = new ArrayList<Course>();
        this.preferences = new StudentPreferences(); // Or default object
    }
    /**
     * Generates all valid schedules by choosing one section per course,
     * checking for time conflicts, and filtering by preferences.
     */
    public Schedule generateSchedule(List<Course> courses) {
        this.courses = courses;
        this.preferences = new StudentPreferences(); // default preferences

        List<Schedule> all = generateSchedules();

        if (all.isEmpty()) {
            return null;
        }

        return all.get(0); // pick the first valid schedule
    }

    public List<Schedule> generateSchedules() {
        List<Schedule> allCombinations = new ArrayList<>();
        List<List<Section>> sectionLists = new ArrayList<>();

        // Convert each course to its list of sections
        for (Course c : courses) {
            sectionLists.add(c.getSections());
        }

        // Temporary builder
        List<Section> current = new ArrayList<>();

        // Recursive generation
        generateCombosRecursive(0, sectionLists, current, allCombinations);

        // Filter valid schedules
        List<Schedule> validSchedules = new ArrayList<>();
        for (Schedule schedule : allCombinations) {
            if (!schedule.hasConflicts() && schedule.respectsPreferences(preferences)) {
                validSchedules.add(schedule);
            }
        }

        return validSchedules;
    }

    /**
     * Recursive helper to generate section combinations.
     */
    private void generateCombosRecursive(
            int index,
            List<List<Section>> sectionLists,
            List<Section> current,
            List<Schedule> result) {

        // Base case: selected one section per course
        if (index == sectionLists.size()) {
            result.add(new Schedule(current));
            return;
        }

        // Choose each section for this course
        for (Section sec : sectionLists.get(index)) {
            current.add(sec);
            generateCombosRecursive(index + 1, sectionLists, current, result);
            current.remove(current.size() - 1);
        }
    }
}
