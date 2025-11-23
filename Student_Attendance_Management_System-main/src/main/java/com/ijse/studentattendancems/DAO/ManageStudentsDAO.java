package com.ijse.studentattendancems.DAO;


import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.MStudent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageStudentsDAO {
    public ManageStudentsDAO() {
    }

    public boolean addStudent(MStudent student) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, reg_no, course_id, contact) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getStudentName());
            stmt.setString(3, student.getRegistrationNo());
            stmt.setString(4, student.getCourseId());
            stmt.setString(5, student.getContactNo());
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean updateStudent(MStudent student) throws SQLException {
        String sql = "UPDATE students SET name = ?, reg_no = ?, course_id = ?, contact = ? WHERE student_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();

        boolean var5;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                stmt.setString(1, student.getStudentName());
                stmt.setString(2, student.getRegistrationNo());
                stmt.setString(3, student.getCourseId());
                stmt.setString(4, student.getContactNo());
                stmt.setString(5, student.getStudentId());
                var5 = stmt.executeUpdate() > 0;
            } catch (Throwable var9) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (Throwable var10) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (connection != null) {
            connection.close();
        }

        return var5;
    }

    public boolean deleteStudent(String studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();

        boolean var5;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                stmt.setString(1, studentId);
                var5 = stmt.executeUpdate() > 0;
            } catch (Throwable var9) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (Throwable var10) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Throwable var7) {
                    var10.addSuppressed(var7);
                }
            }

            throw var10;
        }

        if (connection != null) {
            connection.close();
        }

        return var5;
    }

    public List<MStudent> getAllStudents() throws SQLException {
        List<MStudent> students = new ArrayList();
        String sql = "SELECT * FROM students";
        Connection connection = DbConnection.getInstance().getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                ResultSet rs = stmt.executeQuery();

                try {
                    while(rs.next()) {
                        MStudent student = new MStudent(rs.getString("student_id"), rs.getString("name"), rs.getString("reg_no"), rs.getString("course_id"), rs.getString("contact"));
                        students.add(student);
                    }
                } catch (Throwable var11) {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (Throwable var12) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }

                throw var12;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (Throwable var13) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Throwable var8) {
                    var13.addSuppressed(var8);
                }
            }

            throw var13;
        }

        if (connection != null) {
            connection.close();
        }

        return students;
    }
}
