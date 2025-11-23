package com.ijse.studentattendancems;

import com.ijse.studentattendancems.db.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class UserLoginController {
    @FXML
    private TextField txtID;
    @FXML
    private PasswordField txtPassword;

    public UserLoginController() {
    }

    @FXML
    private void SignOnAction(ActionEvent event) {
        String id = this.txtID.getText().trim();
        String pw = this.txtPassword.getText().trim();
        if (!id.isEmpty() && !pw.isEmpty()) {
            try {
                Connection connection = DbConnection.getInstance().getConnection();
                String sql = "SELECT role FROM users WHERE user_id = ? AND password = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);
                stmt.setString(2, pw);
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    String fxmlFile;
                    String title;
                    if (role.equalsIgnoreCase("ADMIN")) {
                        fxmlFile = "admin_Dashboard.fxml";
                        title = "Admin Dashboard";
                    } else {
                        if (!role.equalsIgnoreCase("LECTURER")) {
                            (new Alert(AlertType.ERROR, "Unknown role.", new ButtonType[0])).show();
                            return;
                        }

                        fxmlFile = "Lecturer_Dashboard.fxml";
                        title = "Lecturer Dashboard";
                    }

                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/ijse/studentattendancems/" + fxmlFile));
                    Parent root = (Parent)loader.load();
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle(title);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    (new Alert(AlertType.ERROR, "Invalid ID or Password", new ButtonType[0])).show();
                }
            } catch (Exception var14) {
                var14.printStackTrace();
                (new Alert(AlertType.ERROR, "Database error: " + var14.getMessage(), new ButtonType[0])).show();
            }

        } else {
            (new Alert(AlertType.ERROR, "Please enter both ID and Password", new ButtonType[0])).show();
        }
    }
}
