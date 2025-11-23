package com.ijse.studentattendancems.DAO;



import com.ijse.studentattendancems.db.DbConnection;
import com.ijse.studentattendancems.model.Lecturer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageLecturersDAO {
    private final Connection connection = DbConnection.getInstance().getConnection();

    public ManageLecturersDAO() throws SQLException {
    }

    public List<Lecturer> getAllLecturers() throws SQLException {
        String query = "SELECT * FROM lecturers";
        List<Lecturer> lecturerList = new ArrayList();
        PreparedStatement stmt = this.connection.prepareStatement(query);

        try {
            ResultSet rs = stmt.executeQuery();

            try {
                while(rs.next()) {
                    String id = rs.getString("lecturer_id");
                    String name = rs.getString("name");
                    String contact = rs.getString("contact");
                    Lecturer lecturer = new Lecturer(id, name, contact);
                    lecturerList.add(lecturer);
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

        return lecturerList;
    }

    public boolean addLecturer(String id, String name, String contact) throws SQLException {
        String query = "INSERT INTO lecturers (lecturer_id, name, contact) VALUES (?, ?, ?)";
        PreparedStatement stmt = this.connection.prepareStatement(query);

        boolean var6;
        try {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, contact);
            var6 = stmt.executeUpdate() > 0;
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

        return var6;
    }

    public boolean updateLecturer(String id, String name, String contact) throws SQLException {
        String query = "UPDATE lecturers SET name = ?, contact = ? WHERE lecturer_id = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);

        boolean var6;
        try {
            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setString(3, id);
            var6 = stmt.executeUpdate() > 0;
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

        return var6;
    }

    public boolean deleteLecturer(String id) throws SQLException {
        String query = "DELETE FROM lecturers WHERE lecturer_id = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);

        boolean var4;
        try {
            stmt.setString(1, id);
            var4 = stmt.executeUpdate() > 0;
        } catch (Throwable var7) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }

            throw var7;
        }

        if (stmt != null) {
            stmt.close();
        }

        return var4;
    }
}
