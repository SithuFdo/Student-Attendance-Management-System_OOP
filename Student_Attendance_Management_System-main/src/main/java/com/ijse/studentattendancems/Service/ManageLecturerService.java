package com.ijse.studentattendancems.Service;


import com.ijse.studentattendancems.DAO.ManageLecturersDAO;
import com.ijse.studentattendancems.model.Lecturer;
import java.sql.SQLException;
import java.util.List;

public class ManageLecturerService {
    private final ManageLecturersDAO manageLecturersDAO = new ManageLecturersDAO();

    public ManageLecturerService() throws SQLException {
    }

    public List<Lecturer> getAllLecturers() throws SQLException {
        return this.manageLecturersDAO.getAllLecturers();
    }

    public boolean addLecturer(String id, String name, String contact) throws SQLException {
        return this.manageLecturersDAO.addLecturer(id, name, contact);
    }

    public boolean updateLecturer(String id, String name, String contact) throws SQLException {
        return this.manageLecturersDAO.updateLecturer(id, name, contact);
    }

    public boolean deleteLecturer(String id) throws SQLException {
        return this.manageLecturersDAO.deleteLecturer(id);
    }
}
