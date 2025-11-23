package com.ijse.studentattendancems.Service;

import com.ijse.studentattendancems.DAO.ScheduleClassDAO;
import com.ijse.studentattendancems.model.ClassSchedule;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import javafx.collections.ObservableList;

public class ScheduleClassService {
    private final ScheduleClassDAO dao;

    public ScheduleClassService() throws SQLException {
        this.dao = new ScheduleClassDAO();
    }

    public List<ClassSchedule> getAllClassSchedules() throws SQLException {
        return this.dao.getAllClassSchedules();
    }

    public int createClassSchedule(ClassSchedule schedule) throws SQLException, IllegalArgumentException {
        if (schedule.getCourseId() > 0 && schedule.getSubjectId() > 0 && schedule.getLecturerId() > 0) {
            if (schedule.getClassDate() != null && !schedule.getClassDate().isEmpty() && schedule.getStartTime() != null && !schedule.getStartTime().isEmpty() && schedule.getEndTime() != null && !schedule.getEndTime().isEmpty()) {
                try {
                    LocalTime start = LocalTime.parse(schedule.getStartTime());
                    LocalTime end = LocalTime.parse(schedule.getEndTime());
                    if (!end.isAfter(start)) {
                        throw new IllegalArgumentException("End time must be after start time.");
                    }
                } catch (Exception var4) {
                    throw new IllegalArgumentException("Invalid time format. Use HH:mm:ss");
                }

                return this.dao.createClassSchedule(schedule); // Updated to match DAO method
            } else {
                throw new IllegalArgumentException("Date, Start Time, and End Time must be specified.");
            }
        } else {
            throw new IllegalArgumentException("Invalid Course, Subject, or Lecturer Id.");
        }
    }

    public boolean updateClassSchedule(ClassSchedule schedule) throws SQLException, IllegalArgumentException {
        if (schedule.getClassId() <= 0) {
            throw new IllegalArgumentException("Class ID must be valid.");
        } else {
            try {
                LocalTime start = LocalTime.parse(schedule.getStartTime());
                LocalTime end = LocalTime.parse(schedule.getEndTime());
                if (!end.isAfter(start)) {
                    throw new IllegalArgumentException("End time must be after start time.");
                }
            } catch (Exception var4) {
                throw new IllegalArgumentException("Invalid time format. Use HH:mm:ss");
            }

            return this.dao.updateClassSchedule(schedule);
        }
    }

    public ObservableList<String> getCourseIds() throws SQLException {
        return this.dao.getCourseIds();
    }

    public ObservableList<String> getSubjectIds() throws SQLException {
        return this.dao.getSubjectIds();
    }

    public ObservableList<String> getLecturerIds() throws SQLException {
        return this.dao.getLecturerIds();
    }
}
