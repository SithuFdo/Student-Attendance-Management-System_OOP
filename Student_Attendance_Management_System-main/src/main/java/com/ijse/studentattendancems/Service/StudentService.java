package com.ijse.studentattendancems.Service;



import com.ijse.studentattendancems.DAO.ManageStudentsDAO;
import com.ijse.studentattendancems.model.MStudent;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final ManageStudentsDAO studentsDAO = new ManageStudentsDAO();

    public StudentService() throws SQLException {
    }

    public boolean addStudent(MStudent student) throws SQLException {
        return this.studentsDAO.addStudent(student);
    }

    public boolean updateStudent(MStudent student) throws SQLException {
        return this.studentsDAO.updateStudent(student);
    }

    public boolean deleteStudent(String studentId) throws SQLException {
        return this.studentsDAO.deleteStudent(studentId);
    }

    public List<MStudent> getAllStudents() throws SQLException {
        return this.studentsDAO.getAllStudents();
    }
}
