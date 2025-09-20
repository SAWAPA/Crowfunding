import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.*;

public class ProjectListView extends Application {

    private TableView<Project> projectTable = new TableView<>();
    private TextField searchField = new TextField();
    private ComboBox<String> categoryFilter = new ComboBox<>();
    private ComboBox<String> sortOptions = new ComboBox<>();

    private final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "Sawapass79";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("หน้ารวมโครงการ");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        searchField.setPromptText("ค้นหาชื่อโครงการ...");
        categoryFilter.setItems(FXCollections.observableArrayList("ทั้งหมด", "เทคโนโลยี", "ศิลปะ", "เกม"));
        categoryFilter.setValue("ทั้งหมด");
        sortOptions.setItems(FXCollections.observableArrayList("ใหม่สุด", "ใกล้หมดเวลา", "ระดมได้มากสุด"));
        sortOptions.setValue("ใหม่สุด");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> loadProjectData());
        categoryFilter.setOnAction(e -> loadProjectData());
        sortOptions.setOnAction(e -> loadProjectData());

        HBox topControls = new HBox(10, 
            new Label("ค้นหา:"), searchField, 
            new Label("หมวดหมู่:"), categoryFilter, 
            new Label("เรียงลำดับ:"), sortOptions
        );
        layout.setTop(topControls);
        
        TableColumn<Project, String> nameCol = new TableColumn<>("ชื่อโครงการ");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Project, String> categoryCol = new TableColumn<>("หมวดหมู่");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(100);

        TableColumn<Project, Double> goalCol = new TableColumn<>("เป้าหมาย");
        goalCol.setCellValueFactory(new PropertyValueFactory<>("goal"));
        goalCol.setPrefWidth(100);
        
        TableColumn<Project, Double> currentFundingCol = new TableColumn<>("ระดมได้");
        currentFundingCol.setCellValueFactory(new PropertyValueFactory<>("current"));
        currentFundingCol.setPrefWidth(100);

        TableColumn<Project, String> deadlineCol = new TableColumn<>("วันสิ้นสุด");
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        deadlineCol.setPrefWidth(120);
        
        projectTable.getColumns().addAll(nameCol, categoryCol, goalCol, currentFundingCol, deadlineCol);
        layout.setCenter(projectTable);

        // โหลดข้อมูลเริ่มต้น
        loadProjectData();

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void loadProjectData() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String search = searchField.getText();
        String category = categoryFilter.getValue();
        String sortOption = sortOptions.getValue();

        String sql = "SELECT p.project_name, c.category_name, p.funding_goal, p.current_funding, p.deadline::text as deadline_str " +
             "FROM projects p " +
             "INNER JOIN categories c ON p.category_id = c.category_id " +
             "WHERE 1=1";
        
        // เพิ่มเงื่อนไขการค้นหา
        if (search != null && !search.trim().isEmpty()) {
            sql += " AND p.project_name ILIKE ?";
        }
        
        if (category != null && !"ทั้งหมด".equals(category)) {
            sql += " AND c.category_name = ?";
        }
        
        if ("ใหม่สุด".equals(sortOption)) {
            sql += " ORDER BY p.deadline DESC";
        } else if ("ใกล้หมดเวลา".equals(sortOption)) {
            sql += " ORDER BY p.deadline ASC";
        } else if ("ระดมได้มากสุด".equals(sortOption)) {
            sql += " ORDER BY p.current_funding DESC";
        }

        System.out.println("=== กำลังโหลดข้อมูล ===");
        System.out.println("SQL: " + sql);
        System.out.println("Search: '" + search + "'");
        System.out.println("Category: '" + category + "'");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("เชื่อมต่อฐานข้อมูลสำเร็จ");

            // ตั้งค่า parameters
            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + search.trim() + "%");
                System.out.println("Parameter " + (paramIndex-1) + ": %" + search.trim() + "%");
            }
            if (category != null && !"ทั้งหมด".equals(category)) {
                stmt.setString(paramIndex++, category);
                System.out.println("Parameter " + (paramIndex-1) + ": " + category);
            }

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            
            while (rs.next()) {
                count++;
                try {
                    String projectName = rs.getString("project_name");
                    String categoryName = rs.getString("category_name");
                    double fundingGoal = rs.getDouble("funding_goal");
                    double currentFunding = rs.getDouble("current_funding");
                    String deadline = rs.getString("deadline_str");
                    
                    System.out.println("Row " + count + ": " + projectName + " | " + categoryName + 
                                     " | " + currentFunding + "/" + fundingGoal + " | " + deadline);
                    
                    projects.add(new Project(projectName, categoryName, fundingGoal, currentFunding, deadline));
                    
                } catch (SQLException e) {
                    System.err.println("Error reading row " + count + ": " + e.getMessage());
                }
            }
            
            System.out.println("พบข้อมูล " + count + " รายการ");
            
        } catch (SQLException e) {
            System.err.println("เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            // ถ้าเชื่อมต่อไม่ได้ ให้ใช้ข้อมูลทดสอบ
            System.out.println("ใช้ข้อมูลทดสอบแทน...");
            projects.addAll(FXCollections.observableArrayList(
                new Project("Smart Home IoT", "เทคโนโลยี", 50000, 12000, "2025-12-31"),
                new Project("AI Robot Helper", "เทคโนโลยี", 80000, 80000, "2025-11-30"),
                new Project("Digital Art Gallery", "ศิลปะ", 30000, 15000, "2025-10-15")
            ));
        }
        
        // อัพเดตตาราง
        projectTable.setItems(projects);
        System.out.println("อัพเดตตารางแล้ว จำนวน: " + projects.size() + " รายการ");
        System.out.println("==================");
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ทำให้ Project class เป็น static เพื่อไม่ให้เกิดปัญหา
    public static class Project {
        private String name;
        private String category;
        private double goal;
        private double current;
        private String deadline;

        public Project(String name, String category, double goal, double current, String deadline) {
            this.name = name;
            this.category = category;
            this.goal = goal;
            this.current = current;
            this.deadline = deadline;
        }

        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getGoal() { return goal; }
        public double getCurrent() { return current; }
        public String getDeadline() { return deadline; }
        
        @Override
        public String toString() {
            return "Project{name='" + name + "', category='" + category + 
                   "', goal=" + goal + ", current=" + current + ", deadline='" + deadline + "'}";
        }
    }
}