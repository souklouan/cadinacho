package io.cadi.souklou.models;

/**
 * Created by arcadius on 23/08/16.
 */
public class MetaCalendars {

    private String id, school, classroom, calendarJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getCalendarJson() {
        return calendarJson;
    }

    public void setCalendarJson(String calendarJson) {
        this.calendarJson = calendarJson;
    }
}
