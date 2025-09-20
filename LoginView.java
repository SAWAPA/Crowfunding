import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class LoginView extends Application {
    private final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "Sawapass79";

    public static User loggedInUser = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (ไม่ตรวจสอบจริง)");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");
        Label statusLabel = new Label();

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                statusLabel.setText("กรุณากรอก username");
                return;
            }
            // ตรวจสอบ username ในฐานข้อมูล
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement stmt = conn.prepareStatement("SELECT user_id, username FROM users WHERE username = ?")) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    loggedInUser = new User(rs.getInt("user_id"), rs.getString("username"));
                    statusLabel.setText("Login สำเร็จ: " + loggedInUser.getUsername());
                    // เปิด ProjectListView
                    new ProjectListView().start(new Stage());
                    primaryStage.close();
                } else {
                    statusLabel.setText("ไม่พบผู้ใช้");
                }
            } catch (SQLException ex) {
                statusLabel.setText("เกิดข้อผิดพลาดฐานข้อมูล");
                ex.printStackTrace();
            }
        });

        registerBtn.setOnAction(e -> {
            new RegisterView().start(new Stage());
        });

        VBox vbox = new VBox(10, usernameField, passwordField, loginBtn, registerBtn, statusLabel);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox, 300, 220);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}