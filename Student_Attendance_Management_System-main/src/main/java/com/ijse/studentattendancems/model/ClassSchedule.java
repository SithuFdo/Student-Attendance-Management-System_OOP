package com.ijse.studentattendancems.model;


public class ClassSchedule {
    private int classId;
    private int courseId;
    private int subjectId;
    private int lecturerId;
    private String classDate;
    private String startTime;
    private String endTime;

    public ClassSchedule() {
    }

    public ClassSchedule(int classId, int courseId, int subjectId, int lecturerId, String classDate, String startTime, String endTime) {
        this.classId = classId;
        this.courseId = courseId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.classDate = classDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getClassId() {
        return this.classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getLecturerId() {
        return this.lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getClassDate() {
        return this.classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
