package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDao {

    public int saveEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee "
                + "(full_name, current_address, contact_number, email_address, "
                + "education, gender, date_available, desired_position, "
                + "desired_salary, legally_authorized, has_relatives, "
                + "relatives_explanation, signature) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, employee.getFullName());
            statement.setString(2, employee.getCurrentAddress());
            statement.setString(3, employee.getContactNumber());
            statement.setString(4, employee.getEmailAddress());
            statement.setString(5, employee.getEducation());
            statement.setString(6, employee.getGender());
            statement.setDate(7, Date.valueOf(employee.getDateAvailable()));
            statement.setString(8, employee.getDesiredPosition());
            statement.setString(9, employee.getDesiredSalary());
            statement.setBoolean(10, employee.isLegallyAuthorized());
            statement.setBoolean(11, employee.isHasRelatives());
            statement.setString(12, employee.getRelativesExplanation());
            statement.setString(13, employee.getSignature());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public String getAllEmployees() throws SQLException {
        String sql = "SELECT id, full_name, current_address, contact_number, "
                + "email_address, education, gender, date_available, "
                + "desired_position, desired_salary, legally_authorized, "
                + "has_relatives, relatives_explanation, signature "
                + "FROM employee ORDER BY id";

        StringBuilder output = new StringBuilder();

        try (Connection connection = Database.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                output.append("Application #").append(results.getInt("id"))
                        .append("\nName: ").append(results.getString("full_name"))
                        .append("\nAddress: ").append(results.getString("current_address"))
                        .append("\nContact: ").append(results.getString("contact_number"))
                        .append("\nEmail: ").append(results.getString("email_address"))
                        .append("\nEducation: ").append(results.getString("education"))
                        .append("\nGender: ").append(results.getString("gender"))
                        .append("\nAvailable: ").append(results.getDate("date_available"))
                        .append("\nPosition: ").append(results.getString("desired_position"))
                        .append("\nSalary: ").append(results.getString("desired_salary"))
                        .append("\nAuthorized to work: ")
                        .append(results.getBoolean("legally_authorized") ? "Yes" : "No")
                        .append("\nHas company relatives: ")
                        .append(results.getBoolean("has_relatives") ? "Yes" : "No")
                        .append("\nRelatives explanation: ")
                        .append(results.getString("relatives_explanation"))
                        .append("\nSignature: ").append(results.getString("signature"))
                        .append("\n\n");
            }
        }

        return output.length() == 0
                ? "No applications have been saved."
                : output.toString();
    }
}