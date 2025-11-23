package com.ijse.studentattendancems.model;



public class MStudent {
    private String studentId;
    private String studentName;
    private String registrationNo;
    private String courseId;
    private String contactNo;

    public MStudent() {
    }

    public MStudent(String studentId, String studentName, String registrationNo, String courseId, String contactNo) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.registrationNo = registrationNo;
        this.courseId = courseId;
        this.contactNo = contactNo;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRegistrationNo() {
        return this.registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String toString() {
        return "MStudent{studentId='" + this.studentId + "', studentName='" + this.studentName + "', registrationNo='" + this.registrationNo + "', courseId='" + this.courseId + "', contactNo='" + this.contactNo + "'}";
    }
}
