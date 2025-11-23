package com.ijse.studentattendancems.Controllers;


import com.ijse.studentattendancems.Service.AttendanceService;
import com.ijse.studentattendancems.model.Attendance;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AttendanceReportsController {
    @FXML
    private ComboBox<String> cmbStudent;
    @FXML
    private ComboBox<String> cmbCourse;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnExport;
    @FXML
    private TableView<Attendance> tblAttendance;
    @FXML
    private TableColumn<Attendance, LocalDate> colDate;
    @FXML
    private TableColumn<Attendance, String> colStudentId;
    @FXML
    private TableColumn<Attendance, String> colCourse;
    @FXML
    private TableColumn<Attendance, String> colStatus;
    @FXML
    private Button homebtn;
    @FXML
    private Button GoBackbtn;

    @FXML
    private Button GoBackbtn2;

    private final ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();
    private final AttendanceService attendanceService;

    public AttendanceReportsController() {
        PrintStream out;
        try {
            this.attendanceService = new AttendanceService();
            out = System.out;
            Date date = new Date();
            out.println("[" + String.valueOf(date) + "] AttendanceService initialized successfully.");
        } catch (SQLException var2) {
            out = System.err;
            Date date = new Date();
            out.println("[" + String.valueOf(date) + "] Failed to initialize AttendanceService: " + var2.getMessage());
            var2.printStackTrace();
            throw new RuntimeException("Failed to initialize AttendanceService", var2);
        }
    }

    @FXML
    public void initialize() {
        this.colDate.setCellValueFactory((cellData) -> {
            return ((Attendance)cellData.getValue()).dateProperty();
        });
        this.colStudentId.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Attendance)cellData.getValue()).getStudentId());
        });
        this.colCourse.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Attendance)cellData.getValue()).getCourseId());
        });
        this.colStatus.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Attendance)cellData.getValue()).getStatus());
        });

        PrintStream out;
        String dateStr;
        try {
            this.cmbStudent.getItems().addAll(this.attendanceService.getAllStudentIds());
            this.cmbCourse.getItems().addAll(this.attendanceService.getAllCourseIds());
            out = System.out;
            dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Loaded " + this.cmbStudent.getItems().size() + " student IDs and " + this.cmbCourse.getItems().size() + " course IDs.");
        } catch (SQLException var2) {
            out = System.err;
            dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Error loading filter options: " + var2.getMessage());
            var2.printStackTrace();
            this.showAlert("Error", "Failed to load filter options: " + var2.getMessage());
        }

        this.toDate.setValue(LocalDate.now());
        this.fromDate.setValue(LocalDate.now().minusDays(30L));
        this.tblAttendance.setItems(this.attendanceList);
        this.loadInitialAttendanceData();
    }

    private void loadInitialAttendanceData() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("[" + String.valueOf(date) + "] Attempting to load initial attendance data...");

        String dateStr;
        try {
            this.attendanceList.setAll(this.attendanceService.getAttendanceByFilters((String)null, (String)null, (LocalDate)this.fromDate.getValue(), (LocalDate)this.toDate.getValue()));
            this.tblAttendance.refresh();
            out = System.out;
            dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Loaded " + this.attendanceList.size() + " attendance records.");
            this.attendanceList.forEach((a) -> {
                PrintStream innerOut = System.out;
                String innerDate = String.valueOf(new Date());
                innerOut.println("[" + innerDate + "] Attendance: " + a.getStudentId() + " - " + a.getStatus() + " on " + String.valueOf(a.getDate()));
            });
        } catch (SQLException var2) {
            out = System.err;
            dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Error loading attendance: " + var2.getMessage());
            var2.printStackTrace();
            this.showAlert("Database Error", "Failed to load initial attendance data: " + var2.getMessage());
        }
    }

    @FXML
    private void onFilter() {
        String studentId = (String)this.cmbStudent.getValue();
        String courseId = (String)this.cmbCourse.getValue();
        LocalDate from = (LocalDate)this.fromDate.getValue();
        LocalDate to = (LocalDate)this.toDate.getValue();
        if (from != null && to != null && from.isAfter(to)) {
            this.showAlert("Invalid Date Range", "From date cannot be after To date");
        } else {
            PrintStream out = System.out;
            String dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Filtering attendance | Student: " + studentId + ", Course: " + courseId + ", From: " + String.valueOf(from) + ", To: " + String.valueOf(to));

            try {
                this.attendanceList.setAll(this.attendanceService.getAttendanceByFilters(studentId, courseId, from, to));
                this.tblAttendance.refresh();
                out = System.out;
                dateStr = String.valueOf(new Date());
                out.println("[" + dateStr + "] Filtered to " + this.attendanceList.size() + " records.");
            } catch (SQLException var6) {
                out = System.err;
                dateStr = String.valueOf(new Date());
                out.println("[" + dateStr + "] Error filtering attendance: " + var6.getMessage());
                var6.printStackTrace();
                this.showAlert("Error", "Failed to filter attendance: " + var6.getMessage());
            }
        }
    }

    @FXML
    private void onExport() throws IOException, SQLException {
        if (this.attendanceList.isEmpty()) {
            this.showAlert("Export Failed", "No data to export");
        } else {
            PrintStream out = System.out;
            String dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Exporting " + this.attendanceList.size() + " records...");
            boolean success = this.attendanceService.exportToCSV(this.attendanceList);
            if (success) {
                this.showAlert("Export Successful", "Attendance report exported to CSV file");
            } else {
                this.showAlert("Export Failed", "Failed to export attendance report");
            }
        }
    }

    @FXML
    private void HomeOnAction() {
        this.navigateTo("/com/ijse/studentattendancems/UserLogin.fxml", "Home", this.homebtn);
    }

    @FXML
    private void GoBackOnAction() {
        this.navigateTo("/com/ijse/studentattendancems/admin_Dashboard.fxml", "Admin Dashboard", this.GoBackbtn);
    }

    @FXML
    private void GoBackOnAction2() {
        this.navigateTo("/com/ijse/studentattendancems/Lecturer_Dashboard.fxml", "Lecturer Dashboard", this.GoBackbtn);
    }


    private void navigateTo(String fxmlPath, String title, Button sourceButton) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource(fxmlPath));
            Stage stage = (Stage)sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException var6) {
            PrintStream out = System.err;
            String dateStr = String.valueOf(new Date());
            out.println("[" + dateStr + "] Navigation error: " + var6.getMessage());
            var6.printStackTrace();
            this.showAlert("Navigation Error", "Could not load " + title + ": " + var6.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        System.out.println("[" + String.valueOf(new Date()) + "] " + title + ": " + message);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}