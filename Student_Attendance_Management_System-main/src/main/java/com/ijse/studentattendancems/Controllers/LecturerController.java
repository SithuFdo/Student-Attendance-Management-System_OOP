package com.ijse.studentattendancems.Controllers;

import com.ijse.studentattendancems.Service.LecturerService;
import com.ijse.studentattendancems.model.LecturerD;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class LecturerController {
    @FXML
    private TextField txtassigsub;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<LecturerD> tblAttendance;
    @FXML
    private TableColumn<LecturerD, String> colDate;
    @FXML
    private TableColumn<LecturerD, String> colStudentId;
    @FXML
    private TableColumn<LecturerD, String> colCourseID;
    @FXML
    private TableColumn<LecturerD, String> colStatus;
    @FXML
    private Button ScheduleClassOnAction;
    @FXML
    private Button btnMarkAttendance;
    @FXML
    private Button btnViewReport;
    @FXML
    private Button homebtn;
    private final ObservableList<LecturerD> attendanceList = FXCollections.observableArrayList();
    private LecturerService lecturerService;

    public LecturerController() {
    }

    @FXML
    public void initialize() {
        PrintStream out = System.out;
        try {
            this.lecturerService = new LecturerService();
            Date date = new Date();
            out.println("[" + String.valueOf(date) + "] LecturerService initialized successfully.");
        } catch (SQLException var2) {
            out = System.err;
            Date date = new Date();
            out.println("[" + String.valueOf(date) + "] Failed to initialize LecturerService: " + var2.getMessage());
            this.showAlert(AlertType.ERROR, "Database Error", "Failed to initialize services");
            return;
        }

        this.colDate.setCellValueFactory((cellData) -> {
            LecturerD record = (LecturerD)cellData.getValue();
            return record != null ? record.dateProperty() : null;
        });
        this.colStudentId.setCellValueFactory((cellData) -> {
            LecturerD record = (LecturerD)cellData.getValue();
            return record != null ? record.studentIdProperty() : null;
        });
        this.colCourseID.setCellValueFactory((cellData) -> {
            LecturerD record = (LecturerD)cellData.getValue();
            return record != null ? record.courseIdProperty() : null;
        });
        this.colStatus.setCellValueFactory((cellData) -> {
            LecturerD record = (LecturerD)cellData.getValue();
            return record != null ? record.statusProperty() : null;
        });
        this.colStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        this.colStatus.setOnEditCommit((event) -> {
            LecturerD record = (LecturerD)event.getRowValue();
            if (record != null) {
                record.setStatus((String)event.getNewValue());
                this.saveAttendanceToDatabase(record);
            }
        });
        this.tblAttendance.setEditable(true);
        this.tblAttendance.setItems(this.attendanceList);
        this.attendanceList.addListener(new ListChangeListener<LecturerD>() {
            @Override
            public void onChanged(Change<? extends LecturerD> change) {
                PrintStream out = System.out;
                Date date = new Date();
                out.println("[" + String.valueOf(date) + "] Attendance list changed. Current size: " + attendanceList.size());
                tblAttendance.refresh();
            }
        });
        this.loadAllAttendance();
    }

    private void loadAllAttendance() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("[" + String.valueOf(date) + "] Attempting to load all attendance records.");
        this.getAllAttendance();
    }

    private void getAllAttendance() {
        PrintStream out = System.out;
        Date date = new Date();
        out.println("[" + String.valueOf(date) + "] Fetching all attendance records from database.");

        try {
            List<LecturerD> attendanceRecords = this.lecturerService.getAllAttendance();
            if (attendanceRecords == null || attendanceRecords.isEmpty()) {
                out = System.out;
                date = new Date();
                out.println("[" + String.valueOf(date) + "] No attendance records found in database.");
                this.showAlert(AlertType.INFORMATION, "No Data", "No attendance records available.");
                this.attendanceList.clear();
                this.tblAttendance.refresh();
                return;
            }

            attendanceRecords.forEach((record) -> {
                PrintStream outInner = System.out;
                Date dateInner = new Date();
                outInner.println("[" + String.valueOf(dateInner) + "] Loaded attendance: " + String.valueOf(record));
                if (record.dateProperty() == null || record.studentIdProperty() == null || record.courseIdProperty() == null || record.statusProperty() == null) {
                    outInner = System.err;
                    dateInner = new Date();
                    outInner.println("[" + dateInner + "] Warning: Null property detected in record: " + String.valueOf(record));
                }
            });
            this.attendanceList.setAll(attendanceRecords);
            this.tblAttendance.setItems(this.attendanceList);
            this.tblAttendance.refresh();
            out = System.out;
            date = new Date();
            out.println("[" + String.valueOf(date) + "] Loaded " + this.attendanceList.size() + " attendance records.");
        } catch (SQLException var2) {
            out = System.err;
            date = new Date();
            out.println("[" + String.valueOf(date) + "] SQLException occurred during attendance loading: " + var2.getMessage());
            var2.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Failed to load attendance records: " + var2.getMessage());
            this.attendanceList.clear();
            this.tblAttendance.refresh();
        }
    }

    @FXML
    private void ScheduleClassOnAction (ActionEvent event) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/com/ijse/studentattendancems/ScheduleClasses.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Schedule Classes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException var4) {
            this.showAlert(AlertType.ERROR, "Navigation Error", "Cannot open Schedule Classes: " + var4.getMessage());
        }
    }


    @FXML
    private void markAttendanceOnAction(ActionEvent event) {
        String subject = this.txtassigsub.getText().trim();
        LocalDate date = (LocalDate)this.datePicker.getValue();
        if (!subject.isEmpty() && date != null) {
            try {
                this.attendanceList.clear();
                List<String> studentIds = this.lecturerService.getStudentsForCourse(subject);
                Iterator var5 = studentIds.iterator();

                while(var5.hasNext()) {
                    String studentId = (String)var5.next();
                    LecturerD record = new LecturerD(date.toString(), studentId, subject, "");
                    this.attendanceList.add(record);
                    PrintStream out = System.out;
                    Date dateOut = new Date();
                    out.println("[" + String.valueOf(dateOut) + "] Added to attendance list: " + String.valueOf(record));
                }

                if (this.attendanceList.isEmpty()) {
                    this.showAlert(AlertType.INFORMATION, "No Students", "No students enrolled in this course");
                }

                this.tblAttendance.refresh();
            } catch (SQLException var8) {
                this.showAlert(AlertType.ERROR, "Database Error", "Failed to load students: " + var8.getMessage());
            }
        } else {
            this.showAlert(AlertType.WARNING, "Validation Error", "Subject and Date must be filled.");
        }
    }

    @FXML
    private void viewReportOnAction(ActionEvent event) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/com/ijse/studentattendancems/AttendaceReports.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Attendance Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException var4) {
            this.showAlert(AlertType.ERROR, "Navigation Error", "Cannot open Attendance Report: " + var4.getMessage());
        }
    }

    @FXML
    private void HomeOnAction() {
        this.navigateTo("/com/ijse/studentattendancems/UserLogin.fxml", "Home", this.homebtn);
    }

    private void navigateTo(String fxmlPath, String title, Button sourceButton) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource(fxmlPath));
            Stage stage = (Stage)sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException var6) {
            this.showAlert(AlertType.ERROR, "Navigation Error", "Failed to load " + title + ": " + var6.getMessage());
        }
    }

    private void saveAttendanceToDatabase(LecturerD record) {
        try {
            boolean success = this.lecturerService.saveAttendance(record.getDate(), record.getStudentId(), record.getCourseId(), record.getStatus());
            if (success) {
                PrintStream out = System.out;
                Date date = new Date();
                out.println("[" + String.valueOf(date) + "] Attendance saved: " + String.valueOf(record));
                this.getAllAttendance();
            } else {
                this.showAlert(AlertType.ERROR, "Save Failed", "Attendance could not be saved");
            }
        } catch (SQLException var3) {
            this.showAlert(AlertType.ERROR, "Database Error", "Failed to save attendance: " + var3.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
