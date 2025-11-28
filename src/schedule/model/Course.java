package schedule.model;

import java.util.ArrayList;
import java.util.List;


public class Course {
	private String code;
    private String name;
    private int credits;
    private List<Section> sections;

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.sections = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section s) {
        sections.add(s);
    }
}
