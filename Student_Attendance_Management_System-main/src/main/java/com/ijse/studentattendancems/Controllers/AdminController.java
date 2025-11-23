package com.ijse.studentattendancems.Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    private Label adminlbl;
    @FXML
    private Button managecoursesbtn;
    @FXML
    private Button managestudentbtn;
    @FXML
    private Button managelecturersbtn;
    @FXML
    private Button scheduleclassesbtn;
    @FXML
    private Button attendancereportbtn;
    @FXML
    private Button logoutbtn;

    public AdminController() {
    }

    @FXML
    void manageonaction(ActionEvent event) {
        navigateTo("/com/ijse/studentattendancems/managecourses.fxml", "Course Management", (Button) event.getSource());
    }

    @FXML
    void managestudentonaction(ActionEvent event) {
        navigateTo("/com/ijse/studentattendancems/ManageStudents.fxml", "Manage Students", (Button) event.getSource());
    }

    @FXML
    void managelectureronaction(ActionEvent event) {
        navigateTo("/com/ijse/studentattendancems/ManageLecturers.fxml", "Manage Lecturers", (Button) event.getSource());
    }

    @FXML
    void scheduleclassesonaction(ActionEvent event) {
        navigateTo("/com/ijse/studentattendancems/ScheduleClasses.fxml", "Schedule Classes", (Button) event.getSource());
    }

    @FXML
    void attendanceonaction(ActionEvent event) {
        navigateTo("/com/ijse/studentattendancems/AttendanceReports.fxml", "Attendance Report", (Button) event.getSource());
    }

    @FXML
    private void logoutonaction(ActionEvent event) {
        navigateTo("/com/ijse/studentattendancems/UserLogin.fxml", "Home", (Button) event.getSource());
    }

    private void navigateTo(String fxmlPath, String title, Button sourceButton) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to load " + title + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}