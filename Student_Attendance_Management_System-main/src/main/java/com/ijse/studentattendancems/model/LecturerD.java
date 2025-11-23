package com.ijse.studentattendancems.model;



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LecturerD {
    private final StringProperty date;
    private final StringProperty studentId;
    private final StringProperty courseId;
    private final StringProperty status;

    public LecturerD(String date, String studentId, String courseId, String status) {
        this.date = new SimpleStringProperty(date);
        this.studentId = new SimpleStringProperty(studentId);
        this.courseId = new SimpleStringProperty(courseId);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty dateProperty() {
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

    public String getDate() {
        return (String)this.date.get();
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

    public void setDate(String value) {
        this.date.set(value);
    }

    public void setStudentId(String value) {
        this.studentId.set(value);
    }

    public void setCourseId(String value) {
        this.courseId.set(value);
    }

    public void setStatus(String value) {
        this.status.set(value);
    }

    public String toString() {
        String var10000 = this.getDate();
        return "LecturerD{date=" + var10000 + ", studentId=" + this.getStudentId() + ", courseId=" + this.getCourseId() + ", status=" + this.getStatus() + "}";
    }
}
