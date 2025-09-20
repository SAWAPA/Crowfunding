import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StatisticsView extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("หน้าสรุปสถิติ");

        int successfulPledges = 450;
        int rejectedPledges = 25;

        Label titleLabel = new Label("สรุปสถิติการสนับสนุน");
        titleLabel.setFont(new Font("Arial", 24));

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("สำเร็จ (" + successfulPledges + ")", successfulPledges),
                        new PieChart.Data("ถูกปฏิเสธ (" + rejectedPledges + ")", rejectedPledges));
        
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("อัตราส่วนการสนับสนุน");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, chart);

        Scene scene = new Scene(layout, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}