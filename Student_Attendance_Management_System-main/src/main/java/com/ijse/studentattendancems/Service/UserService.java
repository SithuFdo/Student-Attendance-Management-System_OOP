package com.ijse.studentattendancems.Service;


import com.ijse.studentattendancems.DAO.UserDAO;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public UserService() {
    }

    public boolean authenticateUser(String id, String password) {
        try {
            return this.userDAO.validateUser(id, password);
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public String determineUserRole(String id) {
        try {
            return this.userDAO.getUserRole(id);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
