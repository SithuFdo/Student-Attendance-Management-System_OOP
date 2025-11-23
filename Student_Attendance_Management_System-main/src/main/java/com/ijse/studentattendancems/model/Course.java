package com.ijse.studentattendancems.model;



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty courseId;
    private final StringProperty courseName;
    private final StringProperty duration;
    private final StringProperty description;

    public Course(String courseId, String courseName, String duration, String description) {
        this.courseId = new SimpleStringProperty(courseId);
        this.courseName = new SimpleStringProperty(courseName);
        this.duration = new SimpleStringProperty(duration);
        this.description = new SimpleStringProperty(description);
    }

    public String getCourseId() {
        return (String)this.courseId.get();
    }

    public void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    public StringProperty courseIdProperty() {
        return this.courseId;
    }

    public String getCourseName() {
        return (String)this.courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return this.courseName;
    }

    public String getDuration() {
        return (String)this.duration.get();
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public StringProperty durationProperty() {
        return this.duration;
    }

    public String getDescription() {
        return (String)this.description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return this.description;
    }
}
