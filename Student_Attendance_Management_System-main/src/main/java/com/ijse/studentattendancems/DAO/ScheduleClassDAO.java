package com.ijse.studentattendancems.DAO;

import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.ClassSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScheduleClassDAO {
    private final Connection connection = DbConnection.getInstance().getConnection();

    public ScheduleClassDAO() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Failed to get a valid database connection from DbConnection.");
        }
    }

    public List<ClassSchedule> getAllClassSchedules() throws SQLException {
        List<ClassSchedule> list = new ArrayList<>();
        String query = "SELECT class_id, course_id, subject_id, lecturer_id, class_date, start_time, end_time FROM classes";
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                LocalDate classDate = rs.getDate("class_date").toLocalDate();
                list.add(new ClassSchedule(
                        rs.getInt("class_id"),
                        rs.getInt("course_id"),
                        rs.getInt("subject_id"),
                        rs.getInt("lecturer_id"),
                        classDate.toString(), // Convert to String for consistency with model
                        rs.getString("start_time"),
                        rs.getString("end_time")
                ));
            }
        }
        return list;
    }

    public int createClassSchedule(ClassSchedule schedule) throws SQLException {
        String sql = "INSERT INTO classes (course_id, subject_id, lecturer_id, class_date, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, schedule.getCourseId());
            pstmt.setInt(2, schedule.getSubjectId());
            pstmt.setInt(3, schedule.getLecturerId());
            pstmt.setString(4, schedule.getClassDate());
            pstmt.setString(5, schedule.getStartTime());
            pstmt.setString(6, schedule.getEndTime());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    public boolean updateClassSchedule(ClassSchedule schedule) throws SQLException {
        String sql = "UPDATE classes SET course_id=?, subject_id=?, lecturer_id=?, class_date=?, start_time=?, end_time=? WHERE class_id=?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getCourseId());
            pstmt.setInt(2, schedule.getSubjectId());
            pstmt.setInt(3, schedule.getLecturerId());
            pstmt.setString(4, schedule.getClassDate());
            pstmt.setString(5, schedule.getStartTime());
            pstmt.setString(6, schedule.getEndTime());
            pstmt.setInt(7, schedule.getClassId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ObservableList<String> getIds(String tableName, String idColumnName) throws SQLException {
        ObservableList<String> ids = FXCollections.observableArrayList();
        String query = "SELECT " + idColumnName + " FROM " + tableName;
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt(1))); // Assuming IDs are integers
            }
        }
        return ids;
    }

    public ObservableList<String> getCourseIds() throws SQLException {
        return this.getIds("courses", "course_id");
    }

    public ObservableList<String> getSubjectIds() throws SQLException {
        return this.getIds("subjects", "subject_id");
    }

    public ObservableList<String> getLecturerIds() throws SQLException {
        return this.getIds("lecturers", "lecturer_id");
    }
}