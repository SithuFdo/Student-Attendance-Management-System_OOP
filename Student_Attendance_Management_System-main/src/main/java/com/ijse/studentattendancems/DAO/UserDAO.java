package com.ijse.studentattendancems.DAO;



import com.ijse.studentattendancems.db.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public UserDAO() {
    }

    public boolean validateUser(String id, String password) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        boolean var7;
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, id);
            pstm.setString(2, password);
            rs = pstm.executeQuery();
            var7 = rs.next();
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstm != null) {
                pstm.close();
            }

        }

        return var7;
    }

    public String getUserRole(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String var6;
        try {
            String sql = "SELECT role FROM users WHERE username = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, id);
            rs = pstm.executeQuery();
            var6 = rs.next() ? rs.getString("role") : null;
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstm != null) {
                pstm.close();
            }

        }

        return var6;
    }
}
