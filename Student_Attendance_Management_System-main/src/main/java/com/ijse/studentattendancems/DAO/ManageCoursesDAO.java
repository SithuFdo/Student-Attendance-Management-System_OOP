package com.ijse.studentattendancems.DAO;


import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageCoursesDAO {
    public ManageCoursesDAO() {
    }

    public boolean addCourse(String courseId, String courseName, String duration, String description) throws SQLException {
        String sql = "INSERT INTO courses (course_id, course_name, duration, description) VALUES (?, ?, ?, ?)";
        Connection connection = DbConnection.getInstance().getConnection();

        boolean var8;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                stmt.setString(1, courseId);
                stmt.setString(2, courseName);
                stmt.setString(3, duration);
                stmt.setString(4, description);
                var8 = stmt.executeUpdate() > 0;
            } catch (Throwable var12) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
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
                } catch (Throwable var10) {
                    var13.addSuppressed(var10);
                }
            }

            throw var13;
        }

        if (connection != null) {
            connection.close();
        }

        return var8;
    }

    public boolean updateCourse(String courseId, String courseName, String duration, String description) throws SQLException {
        String sql = "UPDATE courses SET course_name = ?, duration = ?, description = ? WHERE course_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();

        boolean var8;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                stmt.setString(1, courseName);
                stmt.setString(2, duration);
                stmt.setString(3, description);
                stmt.setString(4, courseId);
                var8 = stmt.executeUpdate() > 0;
            } catch (Throwable var12) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
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
                } catch (Throwable var10) {
                    var13.addSuppressed(var10);
                }
            }

            throw var13;
        }

        if (connection != null) {
            connection.close();
        }

        return var8;
    }

    public boolean deleteCourse(String courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();

        boolean var5;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                stmt.setString(1, courseId);
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

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList();
        String sql = "SELECT * FROM courses";
        Connection connection = DbConnection.getInstance().getConnection();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            try {
                ResultSet rs = stmt.executeQuery();

                try {
                    while(rs.next()) {
                        Course course = new Course(rs.getString("course_id"), rs.getString("course_name"), rs.getString("duration"), rs.getString("description"));
                        courses.add(course);
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

        return courses;
    }
}
