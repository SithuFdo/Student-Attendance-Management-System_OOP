module com.ijse.studentattendancems {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires lombok;

    opens com.ijse.studentattendancems.model to javafx.base;
    opens com.ijse.studentattendancems to javafx.fxml;
    opens com.ijse.studentattendancems.Controllers to javafx.fxml;  // Add this line
    exports com.ijse.studentattendancems;
    exports com.ijse.studentattendancems.Controllers to javafx.fxml;
}