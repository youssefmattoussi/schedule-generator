package schedule.output;

import schedule.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.*;

public class HtmlCalendarExporter {

    private static final List<DayOfWeek> WEEK_ORDER = Arrays.asList(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    // Calendar display hours (8am to 8pm)
    private static final int START_HOUR = 8;
    private static final int END_HOUR = 20;

    // Colors for classes
    private static final String[] COLORS = {
            "#BBDEFB", "#C8E6C9", "#FFE0B2", "#E1BEE7", "#FFCDD2"
    };

    public static void exportCalendar(Schedule schedule, String fileName) {
        Map<DayOfWeek, List<Section>> dayMap = new HashMap<DayOfWeek, List<Section>>();

        for (int i = 0; i < WEEK_ORDER.size(); i++) {
            dayMap.put(WEEK_ORDER.get(i), new ArrayList<Section>());
        }

        // Assign sections to days
        for (Section s : schedule.getSections()) {
            for (TimeSlot t : s.getTimeSlots()) {
                DayOfWeek d = t.getDay();
                if (dayMap.containsKey(d)) {
                    if (!dayMap.get(d).contains(s)) {
                        dayMap.get(d).add(s);
                    }
                }
            }
        }

        // Sort by start time (bubble sort)
        for (DayOfWeek day : WEEK_ORDER) {
            List<Section> list = dayMap.get(day);

            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    TimeSlot t1 = getTimeSlotForDay(list.get(i), day);
                    TimeSlot t2 = getTimeSlotForDay(list.get(j), day);

                    if (t1.getStart().isAfter(t2.getStart())) {
                        Section temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }

        // Write HTML
        FileWriter writer = null;

        try {
            writer = new FileWriter(fileName);

            writer.write("<!DOCTYPE html>\n<html>\n<head>\n<meta charset='UTF-8'>\n");
            writer.write("<title>Weekly Calendar</title>\n");
            writer.write("<style>\n");

            // Layout Grid
            writer.write("body { font-family: Arial, sans-serif; background:#f5f5f5; }\n");
            writer.write(".calendar { display: grid; grid-template-columns: 80px repeat(5, 1fr); height: 1200px; }\n");
            writer.write(".time-col { border-right:1px solid #ccc; }\n");
            writer.write(".day-col { border-left:1px solid #ccc; position:relative; }\n");
            writer.write(".time-label { height:60px; font-size:12px; color:#555; text-align:right; padding-right:10px; border-bottom:1px solid #eee; }\n");

            // Class block
            writer.write(".class-block { position:absolute; left:10px; right:10px; border-radius:8px; padding:6px; ");
            writer.write("font-size:12px; overflow:hidden; border:1px solid #999; }\n");

            writer.write("</style>\n</head>\n<body>\n");

            writer.write("<h1 style='text-align:center;'>Weekly Class Schedule</h1>\n");
            writer.write("<div class='calendar'>\n");

            // TIME COLUMN
            writer.write("<div class='time-col'>\n");
            for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
                writer.write("<div class='time-label'>" + hour + ":00</div>\n");
            }
            writer.write("</div>\n");

            // DAY COLUMNS
            for (int d = 0; d < WEEK_ORDER.size(); d++) {
                DayOfWeek day = WEEK_ORDER.get(d);

                writer.write("<div class='day-col'>\n");
                writer.write("<div style='text-align:center; background:#eee; padding:4px; font-weight:bold; border-bottom:1px solid #ccc;'>" 
                              + day + "</div>\n");

                List<Section> sections = dayMap.get(day);

                for (int i = 0; i < sections.size(); i++) {
                    Section s = sections.get(i);
                    TimeSlot t = getTimeSlotForDay(s, day);

                    int startMin = (t.getStart().getHour() - START_HOUR) * 60 + t.getStart().getMinute();
                    int endMin = (t.getEnd().getHour() - START_HOUR) * 60 + t.getEnd().getMinute();
                    int height = endMin - startMin;

                    String color = COLORS[Math.abs(s.getCourse().getCode().hashCode()) % COLORS.length];

                    writer.write("<div class='class-block' style='top:" + startMin + "px; height:" + height +
                            "px; background:" + color + ";'>\n");

                    writer.write("<b>" + s.getCourse().getCode() + " (" + s.getSectionId() + ")</b><br>\n");
                    writer.write(t.getStart() + " - " + t.getEnd() + "<br>\n");
                    writer.write(s.getInstructor() + "<br>\n");
                    writer.write("</div>\n");
                }

                writer.write("</div>\n");
            }

            writer.write("</div>\n</body>\n</html>");

        } catch (IOException e) {
            System.out.println("Error writing HTML calendar: " + e.getMessage());
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {}
        }
    }

    private static TimeSlot getTimeSlotForDay(Section s, DayOfWeek day) {
        for (TimeSlot t : s.getTimeSlots()) {
            if (t.getDay().equals(day)) {
                return t;
            }
        }
        return null;
    }
}
