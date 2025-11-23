package com.ijse.studentattendancems.Controllers;



import com.ijse.studentattendancems.Service.ManageLecturerService;
import com.ijse.studentattendancems.model.Lecturer;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ManageLecturersController {
    @FXML
    private TextField txtLecturerId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtContact;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Button homebtn;
    @FXML
    private Button GoBackbtn;
    @FXML
    private TableView<Lecturer> tblLecturers;
    @FXML
    private TableColumn<Lecturer, String> colId;
    @FXML
    private TableColumn<Lecturer, String> colName;
    @FXML
    private TableColumn<Lecturer, String> colContact;
    private final ObservableList<Lecturer> lecturerList = FXCollections.observableArrayList();
    private ManageLecturerService lecturerService;

    public ManageLecturersController() {
    }

    @FXML
    public void initialize() {
        PrintStream var10000 = System.out;
        Date var10001 = new Date();
        var10000.println("Initializing ManageLecturersController at " + String.valueOf(var10001));

        try {
            this.lecturerService = new ManageLecturerService();
            System.out.println("LecturerService initialized successfully.");
        } catch (SQLException var2) {
            var2.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Could not initialize LecturerService.");
            return;
        }

        this.colId.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Lecturer)cellData.getValue()).getLecturerId());
        });
        this.colName.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Lecturer)cellData.getValue()).getName());
        });
        this.colContact.setCellValueFactory((cellData) -> {
            return new SimpleStringProperty(((Lecturer)cellData.getValue()).getContact());
        });
        this.loadAllLecturers();
        this.tblLecturers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.fillForm(newVal);
            }

        });
    }

    private void loadAllLecturers() {
        try {
            List<Lecturer> lecturers = this.lecturerService.getAllLecturers();
            this.lecturerList.setAll(lecturers);
            this.tblLecturers.setItems(this.lecturerList);
            this.tblLecturers.refresh();
            System.out.println("Loaded " + lecturers.size() + " lecturers.");
        } catch (SQLException var2) {
            var2.printStackTrace();
            this.showAlert(AlertType.ERROR, "Database Error", "Failed to load lecturers: " + var2.getMessage());
        }

    }

    private void fillForm(Lecturer lecturer) {
        this.txtLecturerId.setText(lecturer.getLecturerId());
        this.txtName.setText(lecturer.getName());
        this.txtContact.setText(lecturer.getContact());
    }

    @FXML
    private void onSave() {
        PrintStream var10000 = System.out;
        Date var10001 = new Date();
        var10000.println("Save button clicked at " + String.valueOf(var10001));
        String id = this.txtLecturerId.getText().trim();
        String name = this.txtName.getText().trim();
        String contact = this.txtContact.getText().trim();
        if (!id.isEmpty() && !name.isEmpty() && !contact.isEmpty()) {
            new Lecturer(id, name, contact);

            try {
                boolean success = this.lecturerService.addLecturer(id, name, contact);
                if (success) {
                    this.showAlert(AlertType.INFORMATION, "Success", "Lecturer added successfully.");
                    this.loadAllLecturers();
                    this.clearForm();
                } else {
                    this.showAlert(AlertType.ERROR, "Failed", "Failed to add lecturer.");
                }
            } catch (SQLException var6) {
                var6.printStackTrace();
                this.showAlert(AlertType.ERROR, "Database Error", "Error adding lecturer: " + var6.getMessage());
            }

        } else {
            this.showAlert(AlertType.WARNING, "Input Error", "All fields are required.");
        }
    }

    @FXML
    private void onUpdate() {
        PrintStream var10000 = System.out;
        Date var10001 = new Date();
        var10000.println("Update button clicked at " + String.valueOf(var10001));
        Lecturer selected = (Lecturer)this.tblLecturers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            this.showAlert(AlertType.WARNING, "No Selection", "Please select a lecturer to update.");
        } else {
            String id = this.txtLecturerId.getText().trim();
            String name = this.txtName.getText().trim();
            String contact = this.txtContact.getText().trim();
            if (!id.isEmpty() && !name.isEmpty() && !contact.isEmpty()) {
                if (!id.equals(selected.getLecturerId())) {
                    this.showAlert(AlertType.WARNING, "ID Mismatch", "Lecturer ID cannot be changed.");
                } else {
                    selected.setName(name);
                    selected.setContact(contact);

                    try {
                        boolean success = this.lecturerService.updateLecturer(id, name, contact);
                        if (success) {
                            this.showAlert(AlertType.INFORMATION, "Success", "Lecturer updated successfully.");
                            this.loadAllLecturers();
                            this.clearForm();
                        } else {
                            this.showAlert(AlertType.ERROR, "Failed", "Failed to update lecturer.");
                        }
                    } catch (SQLException var6) {
                        var6.printStackTrace();
                        this.showAlert(AlertType.ERROR, "Database Error", "Error updating lecturer: " + var6.getMessage());
                    }

                }
            } else {
                this.showAlert(AlertType.WARNING, "Input Error", "All fields are required.");
            }
        }
    }

    @FXML
    private void onDelete() {
        PrintStream var10000 = System.out;
        Date var10001 = new Date();
        var10000.println("Delete button clicked at " + String.valueOf(var10001));
        Lecturer selected = (Lecturer)this.tblLecturers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            this.showAlert(AlertType.WARNING, "No Selection", "Please select a lecturer to delete.");
        } else {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete lecturer " + selected.getName() + "?", new ButtonType[]{ButtonType.YES, ButtonType.NO});
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText((String)null);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                try {
                    boolean success = this.lecturerService.deleteLecturer(selected.getLecturerId());
                    if (success) {
                        this.showAlert(AlertType.INFORMATION, "Success", "Lecturer deleted successfully.");
                        this.loadAllLecturers();
                        this.clearForm();
                    } else {
                        this.showAlert(AlertType.ERROR, "Failed", "Failed to delete lecturer.");
                    }
                } catch (SQLException var4) {
                    var4.printStackTrace();
                    this.showAlert(AlertType.ERROR, "Database Error", "Error deleting lecturer: " + var4.getMessage());
                }

            }
        }
    }

    @FXML
    private void onClear() {
        PrintStream var10000 = System.out;
        Date var10001 = new Date();
        var10000.println("Clear button clicked at " + String.valueOf(var10001));
        this.clearForm();
    }

    private void clearForm() {
        this.txtLecturerId.clear();
        this.txtName.clear();
        this.txtContact.clear();
        this.tblLecturers.getSelectionModel().clearSelection();
    }

    @FXML
    private void HomeOnAction() {
        try {
            PrintStream var10000 = System.out;
            Date var10001 = new Date();
            var10000.println("Home button clicked at " + String.valueOf(var10001));
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/com/ijse/studentattendancems/UserLogin.fxml"));
            Stage stage = (Stage)this.homebtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException var3) {
            var3.printStackTrace();
            this.showAlert(AlertType.ERROR, "Navigation Error", "Could not load Home screen.");
        }

    }

    @FXML
    private void GoBackOnAction() {
        try {
            PrintStream var10000 = System.out;
            Date var10001 = new Date();
            var10000.println("Go Back button clicked at " + String.valueOf(var10001));
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/com/ijse/studentattendancems/admin_Dashboard.fxml"));
            Stage stage = (Stage)this.GoBackbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException var3) {
            var3.printStackTrace();
            this.showAlert(AlertType.ERROR, "Navigation Error", "Could not load Admin Dashboard.");
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
