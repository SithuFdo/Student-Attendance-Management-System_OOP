package com.ijse.studentattendancems.Controllers;

import com.ijse.studentattendancems.Service.StudentService;
import com.ijse.studentattendancems.model.MStudent;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ManageStudentsController {
    @FXML
    private TextField txtStudentId;
    @FXML
    private TextField txtStudentName;
    @FXML
    private TextField txtRegNo;
    @FXML
    private TextField txtCid;
    @FXML
    private TextField txtContact;
    @FXML
    private TableView<MStudent> tblStudents;
    @FXML
    private TableColumn<MStudent, String> colId;
    @FXML
    private TableColumn<MStudent, String> colName;
    @FXML
    private TableColumn<MStudent, String> colRegNo;
    @FXML
    private TableColumn<MStudent, String> colCourse;
    @FXML
    private TableColumn<MStudent, String> colContact;
    @FXML
    private Button homebtn;
    @FXML
    private Button GoBackbtn;
    private final ObservableList<MStudent> studentList = FXCollections.observableArrayList();
    private final StudentService service;

    public ManageStudentsController() {
        PrintStream out;
        try {
            this.service = new StudentService();
            out = System.out;
            Date date = new Date();
            out.println("StudentService initialized successfully at " + String.valueOf(date));
        } catch (SQLException var2) {
            out = System.err;
            Date date = new Date();
            out.println("Failed to initialize StudentService at " + String.valueOf(date) + ": " + var2.getMessage());
            var2.printStackTrace();
            throw new RuntimeException("Failed to initialize StudentService", var2);
        }
    }

    @FXML
    public void initialize() {
        this.colId.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((MStudent)cellData.getValue()).getStudentId());
        });
        this.colName.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((MStudent)cellData.getValue()).getStudentName());
        });
        this.colRegNo.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((MStudent)cellData.getValue()).getRegistrationNo());
        });
        this.colCourse.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((MStudent)cellData.getValue()).getCourseId());
        });
        this.colContact.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((MStudent)cellData.getValue()).getContactNo());
        });
        this.loadAllStudents();
        this.tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.txtStudentId.setText(newVal.getStudentId());
                this.txtStudentName.setText(newVal.getStudentName());
                this.txtRegNo.setText(newVal.getRegistrationNo());
                this.txtCid.setText(newVal.getCourseId());
                this.txtContact.setText(newVal.getContactNo());
            }
        });
    }

    private void loadAllStudents() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Attempting to load all students at " + String.valueOf(date));

        try {
            this.studentList.setAll(this.service.getAllStudents());
            this.studentList.forEach((s) -> {
                PrintStream innerOut = System.out;
                String studentId = s.getStudentId();
                innerOut.println("Loaded student: " + studentId + ", " + s.getStudentName());
            });
            this.tblStudents.setItems(this.studentList);
            this.tblStudents.refresh();
            System.out.println("Loaded " + this.studentList.size() + " students.");
        } catch (SQLException var2) {
            out = System.err;
            String dateStr = String.valueOf(new Date());
            out.println("SQLException occurred during student loading at " + dateStr + ": " + var2.getMessage());
            var2.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Failed to load students from database: " + var2.getMessage());
        }
    }

    @FXML
    private void addOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Add button clicked at " + String.valueOf(date));

        String dateStr;
        try {
            String studentId = this.txtStudentId.getText().trim();
            String name = this.txtStudentName.getText().trim();
            String regNo = this.txtRegNo.getText().trim();
            String courseId = this.txtCid.getText().trim();
            String contact = this.txtContact.getText().trim();
            if (studentId.isEmpty() || name.isEmpty() || regNo.isEmpty() || courseId.isEmpty() || contact.isEmpty()) {
                this.showAlert(AlertType.WARNING, "Input Error", "All fields are required.");
                return;
            }

            MStudent student = new MStudent(studentId, name, regNo, courseId, contact);
            System.out.println("Attempting to add student: " + String.valueOf(student));
            if (this.service.addStudent(student)) {
                this.showAlert(AlertType.INFORMATION, "Success", "Student added successfully.");
                this.loadAllStudents();
                this.clearForm();
            } else {
                this.showAlert(AlertType.ERROR, "Operation Failed", "Failed to add student.");
            }
        } catch (SQLIntegrityConstraintViolationException var7) {
            out = System.err;
            dateStr = String.valueOf(new Date());
            out.println("Add operation failed (Duplicate ID) at " + dateStr + ": " + var7.getMessage());
            var7.printStackTrace();
            this.showAlert(AlertType.ERROR, "Duplicate Entry", "A student with this ID already exists.");
        } catch (SQLException var8) {
            out = System.err;
            dateStr = String.valueOf(new Date());
            out.println("Add operation failed at " + dateStr + ": " + var8.getMessage());
            var8.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Database error during add operation: " + var8.getMessage());
        } catch (Exception var9) {
            System.err.println("An unexpected error occurred during add operation: " + var9.getMessage());
            var9.printStackTrace();
            this.showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + var9.getMessage());
        }
    }

    @FXML
    private void updateOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Update button clicked at " + String.valueOf(date));
        MStudent selected = (MStudent)this.tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            this.showAlert(AlertType.WARNING, "No Selection", "Please select a student to update.");
        } else {
            String dateStr;
            try {
                String studentId = this.txtStudentId.getText().trim();
                String name = this.txtStudentName.getText().trim();
                String regNo = this.txtRegNo.getText().trim();
                String courseId = this.txtCid.getText().trim();
                String contact = this.txtContact.getText().trim();
                if (studentId.isEmpty() || name.isEmpty() || regNo.isEmpty() || courseId.isEmpty() || contact.isEmpty()) {
                    this.showAlert(AlertType.WARNING, "Input Error", "All fields are required.");
                    return;
                }

                if (!studentId.equals(selected.getStudentId())) {
                    this.showAlert(AlertType.WARNING, "ID Mismatch", "Cannot change Student ID during update. Please re-select or adjust.");
                    return;
                }

                selected.setStudentName(name);
                selected.setRegistrationNo(regNo);
                selected.setCourseId(courseId);
                selected.setContactNo(contact);
                System.out.println("Attempting to update student: " + String.valueOf(selected));
                if (this.service.updateStudent(selected)) {
                    this.showAlert(AlertType.INFORMATION, "Success", "Student updated successfully.");
                    this.loadAllStudents();
                    this.clearForm();
                } else {
                    this.showAlert(AlertType.ERROR, "Operation Failed", "Failed to update student. Student not found or no changes made.");
                }
            } catch (SQLIntegrityConstraintViolationException var7) {
                out = System.err;
                dateStr = String.valueOf(new Date());
                out.println("Update operation failed (Constraint Violation) at " + dateStr + ": " + var7.getMessage());
                var7.printStackTrace();
                this.showAlert(AlertType.ERROR, "Constraint Violation", "Update failed due to a database constraint: " + var7.getMessage());
            } catch (SQLException var8) {
                out = System.err;
                dateStr = String.valueOf(new Date());
                out.println("Update operation failed at " + dateStr + ": " + var8.getMessage());
                var8.printStackTrace();
                this.showAlert(AlertType.ERROR, "Database Error", "Database error during update operation: " + var8.getMessage());
            } catch (Exception var9) {
                System.err.println("An unexpected error occurred during update operation: " + var9.getMessage());
                var9.printStackTrace();
                this.showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + var9.getMessage());
            }
        }
    }

    @FXML
    private void deleteOnAction() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("Delete button clicked at " + String.valueOf(date));
        MStudent selected = (MStudent)this.tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            this.showAlert(AlertType.WARNING, "No Selection", "Please select a student to delete.");
        } else {
            Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete student " + selected.getStudentName() + "?", new ButtonType[]{ButtonType.YES, ButtonType.NO});
            confirmationAlert.setHeaderText("Confirm Deletion");
            confirmationAlert.setTitle("Confirm");
            if (confirmationAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                try {
                    System.out.println("Attempting to delete student with ID: " + selected.getStudentId());
                    if (this.service.deleteStudent(selected.getStudentId())) {
                        this.showAlert(AlertType.INFORMATION, "Success", "Student deleted successfully.");
                        this.loadAllStudents();
                        this.clearForm();
                    } else {
                        this.showAlert(AlertType.ERROR, "Operation Failed", "Failed to delete student. Student not found.");
                    }
                } catch (SQLException var4) {
                    out = System.err;
                    String dateStr = String.valueOf(new Date());
                    out.println("Delete operation failed at " + dateStr + ": " + var4.getMessage());
                    var4.printStackTrace();
                    if (var4.getMessage().contains("Cannot delete or update a parent row: a foreign key constraint fails")) {
                        this.showAlert(AlertType.ERROR, "Delete Error", "Cannot delete this student because it is associated with existing attendance records.");
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
        this.txtStudentId.clear();
        this.txtStudentName.clear();
        this.txtRegNo.clear();
        this.txtCid.clear();
        this.txtContact.clear();
        this.tblStudents.getSelectionModel().clearSelection();
    }

    @FXML
    private void HomeOnAction() {
        PrintStream out;
        try {
            out = System.out;
            Date date = new Date();
            out.println("Home button clicked at " + String.valueOf(date));
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/ijse/studentattendancems/UserLogin.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = (Stage)this.homebtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException var4) {
            out = System.err;
            String dateStr = String.valueOf(new Date());
            out.println("Error loading Home view at " + dateStr + ": " + var4.getMessage());
            var4.printStackTrace();
            this.showAlert(AlertType.ERROR, "Navigation Error", "Could not load Home screen.");
        }
    }

    @FXML
    private void GoBackOnAction() {
        PrintStream out;
        try {
            out = System.out;
            Date date = new Date();
            out.println("GoBack button clicked at " + String.valueOf(date));
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/ijse/studentattendancems/admin_Dashboard.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = (Stage)this.GoBackbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException var4) {
            out = System.err;
            String dateStr = String.valueOf(new Date());
            out.println("Error loading Admin Dashboard view at " + dateStr + ": " + var4.getMessage());
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