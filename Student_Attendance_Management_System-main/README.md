Overview of the Project The Student Attendance Management System is a desktop application developed using JavaFX and MySQL. It allows Admins and Lecturers to manage student attendance efficiently. Key features include:
User login for Admins and Lecturers.

Management of students, courses, and lecturers.

Class scheduling and real-time attendance marking.

Attendance report generation. uses JavaFX with FXML for the user interface.

02.Clone the Project

bash Copy Edit git clone https://github.com/Rsk2008/Student_Attendance_Management_System Open in IntelliJ IDEA

Open IntelliJ

Go to File → Open and select the project folder

Configure JavaFX

Go to File → Project Structure → Libraries

Add JavaFX SDK (must be installed separately)

Set Up MySQL Database

Open student_attendance_db.sql in MySQL Workbench

Run the script to create the database and tables

Or use the terminal:

bash Copy Edit mysql -u root -p < student_attendance_db.sql Update DB Credentials in Code Open your DB connection class (DBConnection.java) and update:

java Copy Edit String url = "jdbc:mysql://localhost:3306/student_attendance_db"; String user = "root"; String password = "yourpassword"; Run the Application

Open and run Main.java

Technologies Used Java 11+
JavaFX (for GUI)

FXML (for layout via SceneBuilder)

MySQL (for data storage)

JDBC (for database connectivity)

IntelliJ IDEA (development environment)

Layered Architecture
