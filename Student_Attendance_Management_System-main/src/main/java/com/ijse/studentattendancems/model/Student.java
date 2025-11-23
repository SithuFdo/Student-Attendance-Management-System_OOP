package com.ijse.studentattendancems.model;



public class Student {
    private String studentId;
    private String name;

    public Student() {
    }

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Student{studentId='" + this.studentId + "', name='" + this.name + "'}";
    }
}
