package com.wildcodeschool.wildandwizard.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wildcodeschool.wildandwizard.entity.School;
import com.wildcodeschool.wildandwizard.util.JdbcUtils;

public class SchoolRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/spring_jdbc_quest?serverTimezone=GMT";
    private final static String DB_USER = "h4rryp0tt3r";
    private final static String DB_PASSWORD = "Horcrux4life!";

    public School update(Long id, String name, Long capacity, String country) {

    	Connection connection = null;
    	PreparedStatement request = null;
    	
    	try {
			connection =DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			request = connection.prepareStatement("UPDATE school SET name=?, capacity=?, country=? WHERE id=?;");
			request.setString(1, name);
			request.setLong(2, capacity);
			request.setString(3, country);
			request.setLong(4, id);
			if (request.executeUpdate()!=1) {
				throw new SQLException("failed to update data");
			}
			return new School(id,name,capacity,country);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeStatement(request);
			JdbcUtils.closeConnection(connection);
		}
        return null;
    }

    public School findById(Long id) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );
            statement = connection.prepareStatement(
                    "SELECT * FROM school WHERE id = ?;"
            );
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Long capacity = resultSet.getLong("capacity");
                String country = resultSet.getString("country");
                return new School(id, name, capacity, country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }
        return null;
    }
}
