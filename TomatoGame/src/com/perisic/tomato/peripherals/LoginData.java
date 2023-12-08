package com.perisic.tomato.peripherals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LoginData {

    private Connection connection;

    public LoginData() {
        // Initializing the database connection
        try {
            Class.forName("org.postgresql.Driver");
            String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/user_login";
            String username = "postgres";
            String password = "arpan";
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public void registerUser(String username, String password) {
        if (!userExists(username)) {
            try {
                String insertQuery = "INSERT INTO user_data (username, password) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, hashPassword(password));
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error registering user", e);
            }
        } else {
            throw new UserAlreadyExistsException("Username already taken");
        }
    }

    public boolean checkPassword(String username, String password) {
        try {
            String selectQuery = "SELECT password FROM user_data WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next() && hashPassword(password).equals(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking password", e);
        }
    }

    public boolean userExists(String username) {
        try {
            String countQuery = "SELECT COUNT(*) FROM user_data WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(countQuery)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if user exists", e);
        }
    }

    public UserData getUserData(String username) {
        try {
            String selectQuery = "SELECT username, username FROM user_data WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String fullName = resultSet.getString("username");
                    return new UserData(username, fullName);
                } else {
                    throw new RuntimeException("User data not found for username: " + username);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user data", e);
        }
    }

    public String startSession(String username) {
        // Your session management logic here (if needed)
        return generateSessionToken();
    }

    private String generateSessionToken() {
        // Implement your session token generation logic here (e.g., using UUID)
        return UUID.randomUUID().toString();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
