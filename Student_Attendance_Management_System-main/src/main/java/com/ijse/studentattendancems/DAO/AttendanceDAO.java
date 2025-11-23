package com.ijse.studentattendancems.DAO;



import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.Attendance;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    private final Connection connection = DbConnection.getInstance().getConnection();

    public AttendanceDAO() throws SQLException {
    }

    public List<Attendance> getAttendanceByFilters(String studentId, String courseId, LocalDate fromDate, LocalDate toDate) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM attendance WHERE 1=1");
        if (studentId != null && !studentId.isEmpty()) {
            sql.append(" AND student_id = ?");
        }

        if (courseId != null && !courseId.isEmpty()) {
            sql.append(" AND course_id = ?");
        }

        if (fromDate != null) {
            sql.append(" AND attendance_date >= ?");
        }

        if (toDate != null) {
            sql.append(" AND attendance_date <= ?");
        }

        List<Attendance> attendanceList = new ArrayList();
        PreparedStatement pstm = this.connection.prepareStatement(sql.toString());

        try {
            int paramIndex = 1;
            if (studentId != null && !studentId.isEmpty()) {
                pstm.setString(paramIndex++, studentId);
            }

            if (courseId != null && !courseId.isEmpty()) {
                pstm.setString(paramIndex++, courseId);
            }

            if (fromDate != null) {
                pstm.setDate(paramIndex++, Date.valueOf(fromDate));
            }

            if (toDate != null) {
                pstm.setDate(paramIndex++, Date.valueOf(toDate));
            }

            ResultSet rs = pstm.executeQuery();

            try {
                while(rs.next()) {
                    attendanceList.add(new Attendance(rs.getDate("attendance_date").toLocalDate(), rs.getString("student_id"), String.valueOf(rs.getInt("course_id")), rs.getString("status")));
                }
            } catch (Throwable var14) {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Throwable var13) {
                        var14.addSuppressed(var13);
                    }
                }

                throw var14;
            }

            if (rs != null) {
                rs.close();
            }
        } catch (Throwable var15) {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (Throwable var12) {
                    var15.addSuppressed(var12);
                }
            }

            throw var15;
        }

        if (pstm != null) {
            pstm.close();
        }

        return attendanceList;
    }

    public List<String> getAllStudentIds() throws SQLException {
        List<String> studentIds = new ArrayList();
        String sql = "SELECT student_id FROM student";
        PreparedStatement pstm = this.connection.prepareStatement(sql);

        try {
            ResultSet rs = pstm.executeQuery();

            try {
                while(rs.next()) {
                    studentIds.add(rs.getString("student_id"));
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
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (pstm != null) {
            pstm.close();
        }

        return studentIds;
    }

    public List<String> getAllCourseIds() throws SQLException {
        List<String> courseIds = new ArrayList();
        String sql = "SELECT course_id FROM course";
        PreparedStatement pstm = this.connection.prepareStatement(sql);

        try {
            ResultSet rs = pstm.executeQuery();

            try {
                while(rs.next()) {
                    courseIds.add(rs.getString("course_id"));
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
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (pstm != null) {
            pstm.close();
        }

        return courseIds;
    }
}
