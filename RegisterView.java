import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class RegisterView extends Application {
    private final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "Sawapass79";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Register");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        Label statusLabel = new Label();

        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("กรุณากรอกข้อมูลให้ครบ");
                return;
            }
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();
                statusLabel.setText("ลงทะเบียนสำเร็จ!");
            } catch (SQLException ex) {
                statusLabel.setText("เกิดข้อผิดพลาด: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, usernameField, passwordField, registerBtn, statusLabel);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox, 300, 180);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}