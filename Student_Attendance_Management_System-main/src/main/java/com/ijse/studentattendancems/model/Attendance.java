package com.ijse.studentattendancems.model;



import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Attendance {
    private final ObjectProperty<LocalDate> date;
    private final StringProperty studentId;
    private final StringProperty courseId;
    private final StringProperty status;

    public Attendance(LocalDate date, String studentId, String courseId, String status) {
        this.date = new SimpleObjectProperty(date);
        this.studentId = new SimpleStringProperty(studentId);
        this.courseId = new SimpleStringProperty(courseId);
        this.status = new SimpleStringProperty(status);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return this.date;
    }

    public StringProperty studentIdProperty() {
        return this.studentId;
    }

    public StringProperty courseIdProperty() {
        return this.courseId;
    }

    public StringProperty statusProperty() {
        return this.status;
    }

    public LocalDate getDate() {
        return (LocalDate)this.date.get();
    }

    public String getStudentId() {
        return (String)this.studentId.get();
    }

    public String getCourseId() {
        return (String)this.courseId.get();
    }

    public String getStatus() {
        return (String)this.status.get();
    }
}
