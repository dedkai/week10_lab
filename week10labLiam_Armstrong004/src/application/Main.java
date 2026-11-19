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

        TextField[] fields = {
            field(), field(), field(), field(), field(),
            field(), field(), field(), field()
        };

        DatePicker date = new DatePicker();

        ToggleGroup gender = new ToggleGroup();
        ToggleGroup authorized = new ToggleGroup();
        ToggleGroup relatives = new ToggleGroup();

        HBox genderBox = radios(gender, "Male", "Female", "Other");
        HBox authorizedBox = radios(authorized, "Yes", "No");
        HBox relativesBox = radios(relatives, "Yes", "No");

        int row = 0;

        title(grid, "Employment Application", row++);
        section(grid, "Personal Information", row++);

        String[] personalLabels = {
            "Full Name:", "Current Address:", "Contact Number:",
            "Email Address:", "Education:"
        };

        for (int i = 0; i < personalLabels.length; i++) {
            addRow(grid, personalLabels[i], fields[i], row++);
        }

        addRow(grid, "Gender:", genderBox, row++);

        section(grid, "Employment Eligibility", row++);

        addRow(grid, "Date Available:", date, row++);
        addRow(grid, "Desired Position:", fields[5], row++);
        addRow(grid, "Desired Salary:", fields[6], row++);
        addRow(grid, "Authorized to Work:", authorizedBox, row++);
        addRow(grid, "Company Relatives:", relativesBox, row++);
        addRow(grid, "If Yes, Explain:", fields[7], row++);
        addRow(grid, "Applicant Signature:", fields[8], row++);

        grid.add(new Label(
                "By submitting this application, you agree to provide accurate information."
        ), 0, row++, 2, 1);

        Button clear = new Button("Clear");
        Button save = new Button("Submit Application");
        HBox buttons = new HBox(10, clear, save);

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

        save.setOnAction(event -> {
            if (!valid(fields, date, gender, authorized, relatives)) {
                alert("Error", "Please complete all required fields.",
                        Alert.AlertType.ERROR);
                return;
            }

            Employee employee = new Employee();

            employee.setFullName(fields[0].getText().trim());
            employee.setCurrentAddress(fields[1].getText().trim());
            employee.setContactNumber(fields[2].getText().trim());
            employee.setEmailAddress(fields[3].getText().trim());
            employee.setEducation(fields[4].getText().trim());
            employee.setDateAvailable(date.getValue());
            employee.setDesiredPosition(fields[5].getText().trim());
            employee.setDesiredSalary(fields[6].getText().trim());
            employee.setRelativesExplanation(fields[7].getText().trim());
            employee.setSignature(fields[8].getText().trim());
            employee.setGender(selected(gender));
            employee.setLegallyAuthorized(selected(authorized).equals("Yes"));
            employee.setHasRelatives(selected(relatives).equals("Yes"));

            try {
                int id = dao.saveEmployee(employee);
                alert("Result",
                        id > 0 ? "Application saved successfully."
                               : "Application could not be saved.",
                        id > 0 ? Alert.AlertType.INFORMATION
                               : Alert.AlertType.ERROR);
            } catch (SQLException e) {
                alert("Database Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        stage.setTitle("Employment Application");
        stage.setScene(new Scene(grid, 600, 700));
        stage.show();
    }

    private TextField field() {
        TextField field = new TextField();
        field.setPrefWidth(300);
        return field;
    }

    private void title(GridPane grid, String text, int row) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:24px;-fx-font-weight:bold;");
        grid.add(label, 0, row, 2, 1);
    }

    private void section(GridPane grid, String text, int row) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-background-color:black;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:8px;"
        );
        grid.add(label, 0, row, 2, 1);
    }

    private void addRow(GridPane grid, String label, Node input, int row) {
        grid.add(new Label(label), 0, row);
        grid.add(input, 1, row);
    }

    private HBox radios(ToggleGroup group, String... names) {
        HBox box = new HBox(10);

        for (String name : names) {
            RadioButton radio = new RadioButton(name);
            radio.setToggleGroup(group);
            box.getChildren().add(radio);
        }

        return box;
    }

    private boolean valid(
            TextField[] fields,
            DatePicker date,
            ToggleGroup gender,
            ToggleGroup authorized,
            ToggleGroup relatives) {

        for (TextField field : fields) {
            if (field.getText().isBlank()) {
                return false;
            }
        }

        return date.getValue() != null
                && gender.getSelectedToggle() != null
                && authorized.getSelectedToggle() != null
                && relatives.getSelectedToggle() != null;
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