package com.ijse.studentattendancems.Service;


import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.Attendance;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttendanceService {
    private final Connection connection = DbConnection.getInstance().getConnection();

    public AttendanceService() throws SQLException {
    }

    public List<String> getAllStudentIds() throws SQLException {
        List<String> studentIds = new ArrayList();
        String sql = "SELECT DISTINCT student_id FROM attendance";
        Statement stmt = this.connection.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(sql);

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
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (stmt != null) {
            stmt.close();
        }

        return studentIds;
    }

    public List<String> getAllCourseIds() throws SQLException {
        List<String> courseIds = new ArrayList();
        String sql = "SELECT DISTINCT course_id FROM attendance";
        Statement stmt = this.connection.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(sql);

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
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (stmt != null) {
            stmt.close();
        }

        return courseIds;
    }

    public List<Attendance> getAttendanceByFilters(String studentId, String courseId, LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Attendance> attendanceList = new ArrayList();
        String sql = "SELECT attendance_date, student_id, course_id, status FROM attendance WHERE 1=1";
        List<Object> params = new ArrayList();
        if (studentId != null && !studentId.isEmpty()) {
            sql = sql + " AND student_id = ?";
            params.add(studentId);
        }

        if (courseId != null && !courseId.isEmpty()) {
            sql = sql + " AND course_id = ?";
            params.add(courseId);
        }

        if (fromDate != null) {
            sql = sql + " AND attendance_date >= ?";
            params.add(Date.valueOf(fromDate));
        }

        if (toDate != null) {
            sql = sql + " AND attendance_date <= ?";
            params.add(Date.valueOf(toDate));
        }

        sql = sql + " ORDER BY attendance_date DESC";
        PreparedStatement pst = this.connection.prepareStatement(sql);

        try {
            for(int i = 0; i < params.size(); ++i) {
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pst.executeQuery();

            try {
                while(rs.next()) {
                    attendanceList.add(new Attendance(rs.getDate("attendance_date").toLocalDate(), rs.getString("student_id"), rs.getString("course_id"), rs.getString("status")));
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
            if (pst != null) {
                try {
                    pst.close();
                } catch (Throwable var12) {
                    var15.addSuppressed(var12);
                }
            }

            throw var15;
        }

        if (pst != null) {
            pst.close();
        }

        return attendanceList;
    }

    public boolean exportToCSV(List<Attendance> attendanceList) throws IOException {
        String filename = "AttendanceReport_" + System.currentTimeMillis() + ".csv";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            boolean var9;
            try {
                writer.write("Date,Student ID,Course ID,Status");
                writer.newLine();
                Iterator var4 = attendanceList.iterator();

                while(true) {
                    if (!var4.hasNext()) {
                        var9 = true;
                        break;
                    }

                    Attendance record = (Attendance)var4.next();
                    writer.write(String.format("%s,%s,%s,%s", record.getDate(), record.getStudentId(), record.getCourseId(), record.getStatus()));
                    writer.newLine();
                }
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            writer.close();
            return var9;
        } catch (Exception var8) {
            var8.printStackTrace();
            return false;
        }
    }
}
