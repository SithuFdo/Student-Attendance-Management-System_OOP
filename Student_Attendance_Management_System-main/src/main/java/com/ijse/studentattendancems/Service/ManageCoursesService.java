package com.ijse.studentattendancems.Service;



import com.ijse.studentattendancems.DAO.ManageCoursesDAO;
import com.ijse.studentattendancems.model.Course;
import java.sql.SQLException;
import java.util.List;

public class ManageCoursesService {
    private final ManageCoursesDAO manageCoursesDAO = new ManageCoursesDAO();

    public ManageCoursesService() {
    }

    public boolean addCourse(Course course) throws SQLException {
        return this.isInvalid(course) ? false : this.manageCoursesDAO.addCourse(course.getCourseId(), course.getCourseName(), course.getDuration(), course.getDescription());
    }

    public boolean updateCourse(Course course) throws SQLException {
        return this.isInvalid(course) ? false : this.manageCoursesDAO.updateCourse(course.getCourseId(), course.getCourseName(), course.getDuration(), course.getDescription());
    }

    public boolean deleteCourse(String courseId) throws SQLException {
        return courseId != null && !courseId.trim().isEmpty() ? this.manageCoursesDAO.deleteCourse(courseId) : false;
    }

    public List<Course> getAllCourses() throws SQLException {
        return this.manageCoursesDAO.getAllCourses();
    }

    private boolean isInvalid(Course course) {
        return course == null || this.isEmpty(course.getCourseId()) || this.isEmpty(course.getCourseName()) || this.isEmpty(course.getDuration()) || this.isEmpty(course.getDescription());
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
