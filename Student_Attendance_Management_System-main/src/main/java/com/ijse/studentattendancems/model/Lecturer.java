package com.ijse.studentattendancems.model;



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Lecturer {
    private final StringProperty lecturerId = new SimpleStringProperty(this, "lecturerId", "");
    private final StringProperty name = new SimpleStringProperty(this, "name", "");
    private final StringProperty contact = new SimpleStringProperty(this, "contact", "");

    public Lecturer() {
    }

    public Lecturer(String lecturerId, String name, String contact) {
        this.lecturerId.set(lecturerId);
        this.name.set(name);
        this.contact.set(contact);
    }

    public String getLecturerId() {
        return (String)this.lecturerId.get();
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId.set(lecturerId);
    }

    public StringProperty lecturerIdProperty() {
        return this.lecturerId;
    }

    public String getName() {
        return (String)this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public String getContact() {
        return (String)this.contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public StringProperty contactProperty() {
        return this.contact;
    }

    public String toString() {
        String var10000 = this.getLecturerId();
        return "Lecturer{lecturerId='" + var10000 + "', name='" + this.getName() + "', contact='" + this.getContact() + "'}";
    }
}
