package com.ijse.studentattendancems.Controllers;

import com.ijse.studentattendancems.Service.ManageCoursesService;
import com.ijse.studentattendancems.model.Course;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ManageCoursesController {
    @FXML
    private TextField txtCourseId;
    @FXML
    private TextField txtCourseName;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private TableView<Course> tblCourses;
    @FXML
    private TableColumn<Course, String> colCourseId;
    @FXML
    private TableColumn<Course, String> colCourseName;
    @FXML
    private TableColumn<Course, String> colDuration;
    @FXML
    private TableColumn<Course, String> colDescription;
    @FXML
    private Button homebtn;
    @FXML
    private Button GoBackbtn;
    private final ObservableList<Course> courseList = FXCollections.observableArrayList();
    private final ManageCoursesService manageCoursesService = new ManageCoursesService();

    public ManageCoursesController() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("ManageCoursesService initialized successfully at " + String.valueOf(date));
    }

    @FXML
    public void initialize() {
        this.colCourseId.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Course)cellData.getValue()).getCourseId());
        });
        this.colCourseName.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Course)cellData.getValue()).getCourseName());
        });
        this.colDuration.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Course)cellData.getValue()).getDuration());
        });
        this.colDescription.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Course)cellData.getValue()).getDescription());
        });
        this.loadAllCourses();
        this.tblCourses.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.txtCourseId.setText(newVal.getCourseId());
                this.txtCourseName.setText(newVal.getCourseName());
                this.txtDuration.setText(newVal.getDuration());
                this.txtDescription.setText(newVal.getDescription());
            }
        });
    }

    private void loadAllCourses() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Attempting to load all courses at " + String.valueOf(date));

        try {
            List<Course> courses = this.manageCoursesService.getAllCourses();
            courses.forEach((c) -> {
                PrintStream innerOut = System.out;
                String courseId = c.getCourseId();
                innerOut.println("Loaded course: " + courseId + ", " + c.getCourseName());
            });
            this.courseList.setAll(courses);
            this.tblCourses.setItems(this.courseList);
            this.tblCourses.refresh();
            System.out.println("Loaded " + this.courseList.size() + " courses.");
        } catch (SQLException var2) {
            out = System.err;
            String dateStr = String.valueOf(new Date());
            out.println("SQLException occurred during course loading at " + dateStr + ": " + var2.getMessage());
            var2.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Failed to load courses from database: " + var2.getMessage());
        }
    }

    private void fillForm(Course course) {
        this.txtCourseId.setText(course.getCourseId());
        this.txtCourseName.setText(course.getCourseName());
        this.txtDuration.setText(course.getDuration());
        this.txtDescription.setText(course.getDescription());
    }

    @FXML
    private void addOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Add button clicked at " + String.valueOf(date));

        String dateStr;
        try {
            String courseId = this.txtCourseId.getText().trim();
            String courseName = this.txtCourseName.getText().trim();
            String duration = this.txtDuration.getText().trim();
            String description = this.txtDescription.getText().trim();
            if (courseId.isEmpty() || courseName.isEmpty() || duration.isEmpty() || description.isEmpty()) {
                this.showAlert(AlertType.WARNING, "Input Error", "All fields are required.");
                return;
            }

            Course course = new Course(courseId, courseName, duration, description);
            System.out.println("Attempting to add course: " + String.valueOf(course));
            if (this.manageCoursesService.addCourse(course)) {
                this.showAlert(AlertType.INFORMATION, "Success", "Course added successfully.");
                this.loadAllCourses();
                this.clearForm();
            } else {
                this.showAlert(AlertType.ERROR, "Operation Failed", "Failed to add course.");
            }
        } catch (SQLIntegrityConstraintViolationException var6) {
            out = System.err;
            dateStr = String.valueOf(new Date());
            out.println("Add operation failed (Duplicate ID) at " + dateStr + ": " + var6.getMessage());
            var6.printStackTrace();
            this.showAlert(AlertType.ERROR, "Duplicate Entry", "A course with this ID already exists.");
        } catch (SQLException var7) {
            out = System.err;
            dateStr = String.valueOf(new Date());
            out.println("Add operation failed at " + dateStr + ": " + var7.getMessage());
            var7.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Database error during add operation: " + var7.getMessage());
        } catch (Exception var8) {
            System.err.println("An unexpected error occurred during add operation: " + var8.getMessage());
            var8.printStackTrace();
            this.showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + var8.getMessage());
        }
    }

    @FXML
    private void updateOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Update button clicked at " + String.valueOf(date));
        Course selected = (Course)this.tblCourses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            this.showAlert(AlertType.WARNING, "No Selection", "Please select a course to update.");
        } else {
            try {
                String courseId = this.txtCourseId.getText().trim();
                String courseName = this.txtCourseName.getText().trim();
                String duration = this.txtDuration.getText().trim();
                String description = this.txtDescription.getText().trim();
                if (courseId.isEmpty() || courseName.isEmpty() || duration.isEmpty() || description.isEmpty()) {
                    this.showAlert(AlertType.WARNING, "Input Error", "All fields are required.");
                    return;
                }

                if (!courseId.equals(selected.getCourseId())) {
                    this.showAlert(AlertType.WARNING, "ID Mismatch", "Cannot change Course ID during update. Please re-select or adjust.");
                    return;
                }

                selected.setCourseName(courseName);
                selected.setDuration(duration);
                selected.setDescription(description);
                System.out.println("Attempting to update course: " + String.valueOf(selected));
                if (this.manageCoursesService.updateCourse(selected)) {
                    this.showAlert(AlertType.INFORMATION, "Success", "Course updated successfully.");
                    this.loadAllCourses();
                    this.clearForm();
                } else {
                    this.showAlert(AlertType.ERROR, "Operation Failed", "Failed to update course. Course not found or no changes made.");
                }
            } catch (SQLException var6) {
                out = System.err;
                String dateStr = String.valueOf(new Date());
                out.println("Update operation failed at " + dateStr + ": " + var6.getMessage());
                var6.printStackTrace();
                this.showAlert(AlertType.ERROR, "Database Error", "Database error during update operation: " + var6.getMessage());
            } catch (Exception var7) {
                System.err.println("An unexpected error occurred during update operation: " + var7.getMessage());
                var7.printStackTrace();
                this.showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + var7.getMessage());
            }
        }
    }

    @FXML
    private void deleteOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Delete button clicked at " + String.valueOf(date));
        Course selected = (Course)this.tblCourses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            this.showAlert(AlertType.WARNING, "No Selection", "Please select a course to delete.");
        } else {
            Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete course " + selected.getCourseName() + "?", new ButtonType[]{ButtonType.YES, ButtonType.NO});
            confirmationAlert.setHeaderText("Confirm Deletion");
            confirmationAlert.setTitle("Confirm");
            if (confirmationAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                try {
                    System.out.println("Attempting to delete course with ID: " + selected.getCourseId());
                    if (this.manageCoursesService.deleteCourse(selected.getCourseId())) {
                        this.showAlert(AlertType.INFORMATION, "Success", "Course deleted successfully.");
                        this.loadAllCourses();
                        this.clearForm();
                    } else {
                        this.showAlert(AlertType.ERROR, "Operation Failed", "Failed to delete course. Course not found.");
                    }
                } catch (SQLException var4) {
                    out = System.err;
                    String dateStr = String.valueOf(new Date());
                    out.println("Delete operation failed at " + dateStr + ": " + var4.getMessage());
                    var4.printStackTrace();
                    if (var4.getMessage().contains("Cannot delete or update a parent row: a foreign key constraint fails")) {
                        this.showAlert(AlertType.ERROR, "Delete Error", "Cannot delete this course because it is associated with existing students or attendance records.");
                    } else {
                        this.showAlert(AlertType.ERROR, "Database Error", "Database error during delete operation: " + var4.getMessage());
                    }
                } catch (Exception var5) {
                    System.err.println("An unexpected error occurred during delete operation: " + var5.getMessage());
                    var5.printStackTrace();
                    this.showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + var5.getMessage());
                }
            }
        }
    }

    @FXML
    private void clearOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Clear button clicked at " + String.valueOf(date));
        this.clearForm();
    }

    private void clearForm() {
        this.txtCourseId.clear();
        this.txtCourseName.clear();
        this.txtDuration.clear();
        this.txtDescription.clear();
        this.tblCourses.getSelectionModel().clearSelection();
    }

    @FXML
    private void HomeOnAction() {
        try {
            PrintStream out = System.out;
            Date date = new Date();
            out.println("Home button clicked at " + String.valueOf(date));
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/ijse/studentattendancems/UserLogin.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = (Stage)this.homebtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException var4) {
            System.err.println("Error loading Home view: " + var4.getMessage());
            var4.printStackTrace();
            this.showAlert(AlertType.ERROR, "Navigation Error", "Could not load Home screen.");
        }
    }

    @FXML
    private void GoBackOnAction() {
        try {
            PrintStream out = System.out;
            Date date = new Date();
            out.println("GoBack button clicked at " + String.valueOf(date));
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/ijse/studentattendancems/admin_Dashboard.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = (Stage)this.GoBackbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException var4) {
            System.err.println("Error loading Admin Dashboard view: " + var4.getMessage());
            var4.printStackTrace();
            this.showAlert(AlertType.ERROR, "Navigation Error", "Could not load Admin Dashboard screen.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        System.out.println(title + ": " + message + " at " + String.valueOf(new Date()));
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}