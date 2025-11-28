package schedule.input;

import schedule.model.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputManager {

    private Scanner scanner;

    public InputManager() {
        scanner = new Scanner(System.in);
    }

    public List<Course> getUserCourses() {
        List<Course> courses = new ArrayList<Course>();

        System.out.print("How many courses do you want to enter? ");
        int count = readInt();

        for (int i = 0; i < count; i++) {

            boolean courseDone = false;

            while (!courseDone) {
                System.out.println("\n--- Enter Course " + (i + 1) + " ---");

                System.out.print("Course Code (ex: CMSC203): ");
                String code = scanner.nextLine().trim();

                System.out.print("Course Name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Credits: ");
                int credits = readInt();

                System.out.print("Instructor: ");
                String instructor = scanner.nextLine().trim();

                // Create course and its single "section"
                Course c = new Course(code, name, credits);
                Section sec = new Section("001", c, instructor);

                // Days input like: MON/WED or M/W or T/TH
                List<DayOfWeek> days = null;
                while (true) {
                    System.out.print("Days (ex: MON/WED, M/W, T/TH, F): ");
                    String daysText = scanner.nextLine().trim().toUpperCase();

                    days = parseDays(daysText);
                    if (days.isEmpty()) {
                        System.out.println("Invalid days format. Try again.");
                    } else {
                        break;
                    }
                }

                LocalTime start = readTime("Start time (HH:MM): ");
                LocalTime end = readTime("End time (HH:MM): ");

                // Create one TimeSlot per day
                for (DayOfWeek d : days) {
                    TimeSlot ts = new TimeSlot(d, start, end);
                    sec.addTimeSlot(ts);
                }

                c.addSection(sec);

                // Check conflict with already-entered courses
                if (courseConflicts(c, courses)) {
                    System.out.println("❌ This course time conflicts with one of your existing courses.");
                    System.out.println("   Please enter a different time or days for this course.");
                    // loop again for same course index
                } else {
                    courses.add(c);
                    courseDone = true;
                }
            }
        }

        return courses;
    }

    // Check conflicts between a new course and existing ones
    private boolean courseConflicts(Course newCourse, List<Course> existing) {
        for (Course c : existing) {
            for (Section s1 : c.getSections()) {
                for (TimeSlot t1 : s1.getTimeSlots()) {

                    for (Section s2 : newCourse.getSections()) {
                        for (TimeSlot t2 : s2.getTimeSlots()) {
                            if (timeConflict(t1, t2)) {
                                return true;
                            }
                        }
                    }

                }
            }
        }
        return false;
    }

    // Time conflict between two slots (same day & overlapping time)
    private boolean timeConflict(TimeSlot t1, TimeSlot t2) {

        if (!t1.getDay().equals(t2.getDay())) {
            return false;
        }

        int start1 = t1.getStart().getHour() * 60 + t1.getStart().getMinute();
        int end1   = t1.getEnd().getHour()   * 60 + t1.getEnd().getMinute();

        int start2 = t2.getStart().getHour() * 60 + t2.getStart().getMinute();
        int end2   = t2.getEnd().getHour()   * 60 + t2.getEnd().getMinute();

        // Overlap if one starts before the other's end AND ends after the other's start
        if (start1 < end2 && end1 > start2) {
            return true;
        }

        return false;
    }

    // Parse things like: MON/WED, M/W, T/TH, F
    private List<DayOfWeek> parseDays(String text) {
        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        String[] parts = text.split("/");
        for (int i = 0; i < parts.length; i++) {
            String token = parts[i].trim().toUpperCase();

            DayOfWeek d = parseSingleDay(token);
            if (d == null) {
                // If any token is invalid, we treat the whole input as invalid
                days.clear();
                return days;
            }
            if (!days.contains(d)) {
                days.add(d);
            }
        }

        return days;
    }

    // Accepts MON or M, TUE or TU or T, WED or W, THU or TH, FRI or F
    private DayOfWeek parseSingleDay(String token) {
        if (token.equals("MON") || token.equals("M")) return DayOfWeek.MONDAY;
        if (token.equals("TUE") || token.equals("TU") || token.equals("T")) return DayOfWeek.TUESDAY;
        if (token.equals("WED") || token.equals("W")) return DayOfWeek.WEDNESDAY;
        if (token.equals("THU") || token.equals("TH")) return DayOfWeek.THURSDAY;
        if (token.equals("FRI") || token.equals("F")) return DayOfWeek.FRIDAY;

        return null;
    }

    private int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (Exception e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    private LocalTime readTime(String msg) {
        System.out.print(msg);
        while (true) {
            try {
                String text = scanner.nextLine().trim();
                String[] parts = text.split(":");
                int hour = Integer.parseInt(parts[0]);
                int min = Integer.parseInt(parts[1]);
                return LocalTime.of(hour, min);
            } catch (Exception e) {
                System.out.print("Invalid time. Use HH:MM → ");
            }
        }
    }
}
