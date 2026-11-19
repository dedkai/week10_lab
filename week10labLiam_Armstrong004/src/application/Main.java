package application;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final EmployeeDao dao = new EmployeeDao();

    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setAlignment(Pos.TOP_CENTER);

        TextField[] fields = new TextField[9];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new TextField();
            fields[i].setPrefWidth(300);
        }

        DatePicker date = new DatePicker();
        ToggleGroup gender = new ToggleGroup();
        ToggleGroup authorized = new ToggleGroup();
        ToggleGroup relatives = new ToggleGroup();

        int row = 0;
        title(grid, "Employment Application", row++);
        section(grid, "Personal Information", row++);

        String[] labels = {
            "Full Name:", "Current Address:", "Contact Number:",
            "Email Address:", "Education:"
        };
        for (int i = 0; i < labels.length; i++) {
            addRow(grid, labels[i], fields[i], row++);
        }
        addRow(grid, "Gender:", radios(gender, "Male", "Female", "Other"), row++);

        section(grid, "Employment Eligibility", row++);
        addRow(grid, "Date Available:", date, row++);
        addRow(grid, "Desired Position:", fields[5], row++);
        addRow(grid, "Desired Salary:", fields[6], row++);
        addRow(grid, "Authorized to Work:", radios(authorized, "Yes", "No"), row++);
        addRow(grid, "Company Relatives:", radios(relatives, "Yes", "No"), row++);
        addRow(grid, "If Yes, Explain:", fields[7], row++);
        addRow(grid, "Applicant Signature:", fields[8], row++);

        Label agreement = new Label(
                "By submitting, you agree to provide accurate information."
        );
        agreement.setWrapText(true);
        grid.add(agreement, 0, row++, 2, 1);

        Button clear = new Button("Clear");
        Button view = new Button("View Applications");
        Button save = new Button("Submit Application");
        HBox buttons = new HBox(10, view, clear, save);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, row);

        clear.setOnAction(event -> {
            for (TextField field : fields) {
                field.clear();
            }
            date.setValue(null);
            gender.selectToggle(null);
            authorized.selectToggle(null);
            relatives.selectToggle(null);
        });

        view.setOnAction(event -> {
            try {
                TextArea records = new TextArea(dao.getAllEmployees());
                records.setEditable(false);
                records.setWrapText(true);
                records.setPrefSize(450, 300);

                Alert popup = new Alert(Alert.AlertType.INFORMATION);
                popup.setTitle("Saved Applications");
                popup.setHeaderText("Employee applications in the database");
                popup.getDialogPane().setContent(records);
                popup.showAndWait();
            } catch (SQLException e) {
                alert("Database Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        save.setOnAction(event -> {
            if (!valid(fields, date, gender, authorized, relatives)) {
                alert("Incomplete Form", "Complete all required fields.",
                        Alert.AlertType.ERROR);
                return;
            }

            Employee employee = new Employee();
            employee.setFullName(text(fields, 0));
            employee.setCurrentAddress(text(fields, 1));
            employee.setContactNumber(text(fields, 2));
            employee.setEmailAddress(text(fields, 3));
            employee.setEducation(text(fields, 4));
            employee.setDateAvailable(date.getValue());
            employee.setDesiredPosition(text(fields, 5));
            employee.setDesiredSalary(text(fields, 6));
            employee.setRelativesExplanation(text(fields, 7));
            employee.setSignature(text(fields, 8));
            employee.setGender(selected(gender));
            employee.setLegallyAuthorized(selected(authorized).equals("Yes"));
            employee.setHasRelatives(selected(relatives).equals("Yes"));

            try {
                int id = dao.saveEmployee(employee);
                alert(id > 0 ? "Success" : "Error",
                        id > 0 ? "Application saved." : "Application was not saved.",
                        id > 0 ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            } catch (SQLException e) {
                alert("Database Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        stage.setTitle("Employment Application");
        stage.setScene(new Scene(grid, 650, 700));
        stage.show();
    }

    private String text(TextField[] fields, int index) {
        return fields[index].getText().trim();
    }

    private boolean valid(TextField[] fields, DatePicker date, ToggleGroup... groups) {
        for (int i = 0; i < fields.length; i++) {
            if (i != 7 && fields[i].getText().trim().isEmpty()) {
                return false;
            }
        }
        if (date.getValue() == null) return false;
        for (ToggleGroup group : groups) {
            if (group.getSelectedToggle() == null) return false;
        }
        return true;
    }

    private void title(GridPane grid, String text, int row) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:24px;-fx-font-weight:bold;");
        grid.add(label, 0, row, 2, 1);
    }

    private void section(GridPane grid, String text, int row) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color:black;-fx-text-fill:white;"
                + "-fx-font-weight:bold;-fx-padding:8px;");
        grid.add(label, 0, row, 2, 1);
    }

    private void addRow(GridPane grid, String label, Node input, int row) {
        grid.add(new Label(label), 0, row);
        grid.add(input, 1, row);
    }

    private HBox radios(ToggleGroup group, String... options) {
        HBox box = new HBox(10);
        for (String option : options) {
            RadioButton radio = new RadioButton(option);
            radio.setToggleGroup(group);
            box.getChildren().add(radio);
        }
        return box;
    }

    private String selected(ToggleGroup group) {
        return ((RadioButton) group.getSelectedToggle()).getText();
    }

    private void alert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}