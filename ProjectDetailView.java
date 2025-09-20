import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ProjectDetailView extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("หน้ารายละเอียดโครงการ");

        // --- จำลองว่าเราได้ข้อมูลโปรเจกต์มาจากหน้าแรก ---
        String projectName = "เกมใหม่จากสตูดิโออินดี้";
        double currentFunding = 85000.0;
        double fundingGoal = 100000.0;
        String deadline = "2025-11-15";
        String description = "รายละเอียดโปรเจกต์: เกมนี้เป็นเกมแนวผจญภัยในโลกแฟนตาซี...";

        // ----- UI Components -----
        Label nameLabel = new Label(projectName);
        nameLabel.setFont(new Font("Arial", 24));

        Label goalLabel = new Label(String.format("เป้าหมาย: %,.2f บาท", fundingGoal));
        Label currentLabel = new Label(String.format("ระดมทุนได้: %,.2f บาท", currentFunding));
        Label deadlineLabel = new Label("สิ้นสุดวันที่: " + deadline);
        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true); // ทำให้ตัดคำเมื่อข้อความยาว

        // ----- Progress Bar -----
        ProgressBar progressBar = new ProgressBar();
        double progress = currentFunding / fundingGoal;
        progressBar.setProgress(progress);
        progressBar.setPrefWidth(400); // กำหนดความกว้าง

        Label progressText = new Label(String.format("%.0f%%", progress * 100));

        // ----- จัดวาง Layout -----
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
            nameLabel,
            descriptionLabel,
            progressBar,
            progressText,
            currentLabel,
            goalLabel,
            deadlineLabel
        );

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}