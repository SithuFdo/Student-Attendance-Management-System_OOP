package com.ijse.studentattendancems.Service;



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

public class LecturerService {
    private final Connection connection = DbConnection.getInstance().getConnection();

    public LecturerService() throws SQLException {
    }

    public boolean scheduleClass(String subject_id, LocalDate class_date) throws SQLException {
        String sql = "INSERT INTO classes (subject_id, class_date) VALUES (?, ?)";
        PreparedStatement pst = this.connection.prepareStatement(sql);

        boolean var5;
        try {
            pst.setString(1, subject_id);
            pst.setDate(2, Date.valueOf(class_date));
            var5 = pst.executeUpdate() > 0;
        } catch (Throwable var8) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        if (pst != null) {
            pst.close();
        }

        return var5;
    }

    public List<String> getStudentsForCourse(String courseId) throws SQLException {
        List<String> studentIds = new ArrayList();
        String sql = "SELECT student_id FROM enrollments WHERE course_id = ?";
        PreparedStatement pst = this.connection.prepareStatement(sql);

        try {
            pst.setString(1, courseId);
            ResultSet rs = pst.executeQuery();

            try {
                while(rs.next()) {
                    studentIds.add(rs.getString("student_id"));
                }
            } catch (Throwable var10) {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                }

                throw var10;
            }

            if (rs != null) {
                rs.close();
            }
        } catch (Throwable var11) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (Throwable var8) {
                    var11.addSuppressed(var8);
                }
            }

            throw var11;
        }

        if (pst != null) {
            pst.close();
        }

        return studentIds;
    }

    public boolean saveAttendance(String date, String studentId, String courseId, String status) throws SQLException {
        String sql = "INSERT INTO attendance (attendance_date, student_id, course_id, status) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE status = ?";
        PreparedStatement pst = this.connection.prepareStatement(sql);

        boolean var7;
        try {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setString(2, studentId);
            pst.setString(3, courseId);
            pst.setString(4, status);
            pst.setString(5, status);
            var7 = pst.executeUpdate() > 0;
        } catch (Throwable var10) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }

            throw var10;
        }

        if (pst != null) {
            pst.close();
        }

        return var7;
    }

    public List<LecturerD> getAllAttendance() throws SQLException {
        List<LecturerD> attendanceRecords = new ArrayList();
        String sql = "SELECT attendance_date, student_id, course_id, status FROM attendance";
        PreparedStatement pst = this.connection.prepareStatement(sql);

        try {
            ResultSet rs = pst.executeQuery();

            try {
                while(rs.next()) {
                    LecturerD record = new LecturerD(rs.getString("attendance_date"), rs.getString("student_id"), rs.getString("course_id"), rs.getString("status"));
                    attendanceRecords.add(record);
                }
            } catch (Throwable var9) {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (rs != null) {
                rs.close();
            }
        } catch (Throwable var10) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (pst != null) {
            pst.close();
        }

        return attendanceRecords;
    }
}
