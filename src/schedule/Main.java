package schedule;

import schedule.model.*;
import schedule.engine.ScheduleGenerator;
import schedule.input.InputManager;
import schedule.output.ConsolePrinter;
import schedule.output.HtmlCalendarExporter;

import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== Welcome to the Class Schedule Generator ===");

        // 1. Get courses from the user
        InputManager input = new InputManager();
        List<Course> courses = input.getUserCourses();

        // 2. Simple, wide-open preferences (you can tweak later)
        StudentPreferences prefs = new StudentPreferences(
                0,                      // min credits
                99,                     // max credits
                LocalTime.of(8, 0),     // earliest start
                LocalTime.of(22, 0)     // latest end
        );

        // 3. Generate all valid schedules
        ScheduleGenerator generator = new ScheduleGenerator(courses, prefs);
        List<Schedule> schedules = generator.generateSchedules();

        if (schedules.isEmpty()) {
            System.out.println(" No valid schedules found with these courses and preferences.");
            return;
        }

        // For now: choose the first valid schedule
        Schedule chosen = schedules.get(0);

        // 4. Print weekly schedule in console
        ConsolePrinter.printWeekly(chosen);

        // 5. Export beautiful HTML calendar
        HtmlCalendarExporter.exportCalendar(chosen, "calendar.html");
        System.out.println("\nHTML calendar exported → calendar.html");
        System.out.println("Open it in your browser to see the weekly view.");
    }
}
