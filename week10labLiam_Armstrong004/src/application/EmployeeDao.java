package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDao {

    public int saveEmployee(Employee employee) throws SQLException {

        String query =
                "INSERT INTO employee " +
                "(full_name, current_address, contact_number, email_address, " +
                "education, gender, date_available, desired_position, " +
                "desired_salary, legally_authorized, has_relatives, " +
                "relatives_explanation, signature) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = Database.getDBConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
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

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }
}