package com.ijse.studentattendancems.model;



public class AttendanceTM {
    private final String date;
    private final String studentId;
    private final String studentName;
    private String status;
    private String courseId;

    public AttendanceTM(String date, String studentId, String studentName, String status, String courseId) {
        this.date = date;
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
        this.courseId = courseId;
    }

    public String getDate() {
        return this.date;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
