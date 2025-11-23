package com.ijse.studentattendancems.DAO;



import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.LecturerD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {
    private final Connection connection = DbConnection.getInstance().getConnection();

    public LecturerDAO() throws SQLException {
    }

    public boolean saveAttendance(LecturerD lecturerD) throws SQLException {
        String sql = "INSERT INTO attendance (date, student_id, course_id, status) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE status = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        boolean var5;
        try {
            LocalDate localDate = LocalDate.parse(lecturerD.getDate());
            statement.setDate(1, Date.valueOf(localDate));
            statement.setString(2, lecturerD.getStudentId());
            statement.setString(3, lecturerD.getCourseId());
            statement.setString(4, lecturerD.getStatus());
            statement.setString(5, lecturerD.getStatus());
            var5 = statement.executeUpdate() > 0;
        } catch (Throwable var7) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }

            throw var7;
        }

        if (statement != null) {
            statement.close();
        }

        return var5;
    }

}