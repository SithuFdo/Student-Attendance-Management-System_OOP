package com.ijse.studentattendancems.Controllers;

import com.ijse.studentattendancems.Service.ScheduleClassService;
import com.ijse.studentattendancems.model.ClassSchedule;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class SchedulaClassesController implements Initializable {

    @FXML private TextField txtClassId;
    @FXML private ComboBox<String> cbCourseId;
    @FXML private ComboBox<String> cbSubjectId;
    @FXML private ComboBox<String> cbLecturerId;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cbStartTime;
    @FXML private ComboBox<String> cbEndTime;
    @FXML private Button btnSave;
    @FXML private TableView<ClassSchedule> tblSchedules;
    @FXML private TableColumn<ClassSchedule, Integer> colClassId;
    @FXML private TableColumn<ClassSchedule, Integer> colCourseId;
    @FXML private TableColumn<ClassSchedule, Integer> colSubjectId;
    @FXML private TableColumn<ClassSchedule, Integer> colLecturerId;
    @FXML private TableColumn<ClassSchedule, String> colClassDate;
    @FXML private TableColumn<ClassSchedule, String> colStartTime;
    @FXML private TableColumn<ClassSchedule, String> colEndTime;
    @FXML private Button homebtn;
    @FXML private Button GoBackbtn;
    @FXML private Button GoBackbtn2;

    private final ObservableList<ClassSchedule> classList = FXCollections.observableArrayList();
    private final ScheduleClassService scheduleClassService;
    private final ObservableList<String> startTimes = FXCollections.observableArrayList("08:00:00", "09:00:00", "10:00:00", "11:00:00", "13:00:00", "14:00:00", "15:00:00");
    private final ObservableList<String> endTimes = FXCollections.observableArrayList("10:00:00", "11:00:00", "12:00:00", "13:00:00", "15:00:00", "16:00:00", "17:00:00");

    public SchedulaClassesController() {
        try {
            this.scheduleClassService = new ScheduleClassService();
            System.out.println("ScheduleClassService initialized successfully at " + new Date());
        } catch (SQLException e) {
            System.err.println("Failed to initialize ScheduleClassService at " + new Date() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize ScheduleClassService", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colClassId.setCellValueFactory(new PropertyValueFactory<>("classId"));
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colSubjectId.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        colLecturerId.setCellValueFactory(new PropertyValueFactory<>("lecturerId"));
        colClassDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassDate()));
        colStartTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime()));
        colEndTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));

        initComboBoxes();
        loadSchedules();
        btnSave.setDisable(true);
        addFormListeners();
        validateForm(); // validate once initially

        btnSave.setOnAction(event -> SaveOnAction());

        tblSchedules.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadScheduleToForm(newSelection);
            }
        });

        homebtn.setOnAction(event -> HomeOnAction());
        GoBackbtn.setOnAction(event -> GoBackOnAction());
        GoBackbtn2.setOnAction(event -> GoBackOnAction2());
    }
    @FXML
    private void SaveOnAction() {
        try {
            int courseId = Integer.parseInt(cbCourseId.getValue());
            int subjectId = Integer.parseInt(cbSubjectId.getValue());
            int lecturerId = Integer.parseInt(cbLecturerId.getValue());
            String classDate = datePicker.getValue().toString();
            String startTime = cbStartTime.getValue();
            String endTime = cbEndTime.getValue();

            if (startTime.compareTo(endTime) >= 0) {
                throw new IllegalArgumentException("Start time must be before end time.");
            }

            ClassSchedule schedule;
            if (txtClassId.getText().isEmpty()) {
                schedule = new ClassSchedule(0, courseId, subjectId, lecturerId, classDate, startTime, endTime);
                int newId = scheduleClassService.createClassSchedule(schedule);
                txtClassId.setText(String.valueOf(newId));
                showAlert("Success", "Class scheduled successfully and saved to classes table.");
            } else {
                int classId = Integer.parseInt(txtClassId.getText());
                schedule = new ClassSchedule(classId, courseId, subjectId, lecturerId, classDate, startTime, endTime);
                scheduleClassService.updateClassSchedule(schedule);
                showAlert("Success", "Schedule updated successfully in classes table.");
            }

            loadSchedules();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID input: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage());
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save schedule: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Unexpected Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initComboBoxes() {
        cbStartTime.setItems(startTimes);
        cbEndTime.setItems(endTimes);
        try {
            // Corrected typo: should be 'scheduleClassService' not 'schedulaClassService'
            cbCourseId.setItems(scheduleClassService.getCourseIds());
            cbSubjectId.setItems(scheduleClassService.getSubjectIds());
            cbLecturerId.setItems(scheduleClassService.getLecturerIds());
        } catch (SQLException e) {
            System.err.println("Failed to load ComboBox data at " + new Date() + ": " + e.getMessage());
            e.printStackTrace();
            showAlert("Database Error", "Failed to load ComboBox data: " + e.getMessage());
        }
    }

    private void loadSchedules() {
        try {
            classList.setAll(scheduleClassService.getAllClassSchedules());
            tblSchedules.setItems(classList);
            tblSchedules.refresh();
            System.out.println("Loaded " + classList.size() + " schedules.");
        } catch (SQLException e) {
            System.err.println("SQLException occurred during schedule loading: " + e.getMessage());
            e.printStackTrace();
            showAlert("Database Error", "Failed to load schedules from database: " + e.getMessage());
        }
    }

    private void addFormListeners() {
        cbCourseId.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        cbSubjectId.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        cbLecturerId.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        cbStartTime.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        cbEndTime.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }

    private void validateForm() {
        boolean valid = cbCourseId.getValue() != null &&
                cbSubjectId.getValue() != null &&
                cbLecturerId.getValue() != null &&
                datePicker.getValue() != null &&
                cbStartTime.getValue() != null &&
                cbEndTime.getValue() != null;

        if (valid) {
            String start = cbStartTime.getValue();
            String end = cbEndTime.getValue();
            valid = start.compareTo(end) < 0;
        }

        btnSave.setDisable(!valid);
    }

    private void loadScheduleToForm(ClassSchedule schedule) {
        txtClassId.setText(String.valueOf(schedule.getClassId()));
        cbCourseId.setValue(String.valueOf(schedule.getCourseId()));
        cbSubjectId.setValue(String.valueOf(schedule.getSubjectId()));
        cbLecturerId.setValue(String.valueOf(schedule.getLecturerId()));
        datePicker.setValue(LocalDate.parse(schedule.getClassDate()));
        cbStartTime.setValue(schedule.getStartTime());
        cbEndTime.setValue(schedule.getEndTime());
    }

    private void clearForm() {
        txtClassId.clear();
        cbCourseId.getSelectionModel().clearSelection();
        cbSubjectId.getSelectionModel().clearSelection();
        cbLecturerId.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        cbStartTime.getSelectionModel().clearSelection();
        cbEndTime.getSelectionModel().clearSelection();
        btnSave.setDisable(true);
        tblSchedules.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        System.out.println(title + ": " + message + " at " + new Date());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void HomeOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ijse/studentattendancems/UserLogin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) homebtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load Home screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void GoBackOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ijse/studentattendancems/admin_Dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) GoBackbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load Admin Dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    private void GoBackOnAction2() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ijse/studentattendancems/Lecturer_Dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) GoBackbtn2.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lecturer Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load Lecturer Dashboard.");
            e.printStackTrace();
        }
    }
}