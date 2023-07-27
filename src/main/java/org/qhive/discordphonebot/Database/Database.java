package org.qhive.discordphonebot.Database;

import org.qhive.discordphonebot.Util;

import javax.annotation.Nullable;
import java.sql.*;

import static org.qhive.discordphonebot.Util.log;

// /shrd/applications/databases/4506
public class Database {

    private static Connection connection;
    public static void init() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mariadb://" + System.getenv("DB-URL"),
                System.getenv("DB-USER"),
                System.getenv("DB-ACCESS")
        );

        if (!connection.isValid(10)) {
            throw new RuntimeException("Can't connect to database");
        }

        connection.prepareStatement("""
                USE DPBot
                """).execute();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Users
                (
                    id   int PRIMARY KEY AUTO_INCREMENT NOT NULL,
                    user text                           NOT NULL
                );
                """).execute();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Numbers
                (
                    id      int PRIMARY KEY AUTO_INCREMENT NOT NULL,
                    number  text,
                    user_id int                            NOT NULL,
                    CONSTRAINT `fk_user`
                        FOREIGN KEY (user_id) REFERENCES Users(id)
                            ON DELETE CASCADE
                            ON UPDATE RESTRICT
                );
                """).execute();

        connection.prepareStatement("""
                INSERT IGNORE INTO Users (id, user) VALUES (-1, 'null user')
                """).execute();
        connection.prepareStatement("""
                INSERT IGNORE INTO Numbers (id, number, user_id) VALUES (-1, 'null number', -1)
                """).execute();

    }

    public static void addUser(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Users (user) VALUE (?)
                    """);

            statement.setString(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error creating new user");
        }
    }

    public static int getUserDbId(String userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT id FROM Users where user = ?
                    """);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return resultSet.getInt(1);
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error getting user");
        }
    }

    public static void assignNumber(String number, String userId) {
        try {
            if (getNumberDbID(number) == -1) {
                log("the number cannot be assigned due to it not existing in the database." +
                         " You might want to add it");
                return;
            }
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Numbers SET user_id = ? WHERE number = ?
                    """);
            statement.setInt(1, getUserDbId(userId));
            statement.setString(2, number);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error assigning user to number");
        }
    }

    public static void addNumber(String number) {
        try {
            if (getNumberDbID(number) != -1) return;
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Numbers (number, user_id) VALUES (?, -1)
                    """);
            statement.setString(1, number);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error adding number to database");
        }
    }

    public static int getNumberDbID(String number) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT id FROM Numbers WHERE number = ?
                    """);
            statement.setString(1, number);
            ResultSet result = statement.executeQuery();
            if (result.next()) return result.getInt(1);
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error retrieving number from database");
        }
    }

    public static String getNextAvailableNumber() {
        try {
            ResultSet result = connection.prepareStatement("""
                    SELECT number FROM Numbers WHERE user_id = -1 AND NOT id = -1
                    """).executeQuery();
            if (!result.next()) return null;
            return result.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error getting next available number");
        }
    }

    @Nullable
    public static String getUserFromNumber(String number) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT user FROM Numbers n LEFT JOIN Users u on n.user_id = u.id WHERE number = ?
                    """);
            statement.setString(1, number);

            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                log("Number not in database" + number);
                return null; // TODO: better error handling
            }
            String user_id = result.getString(1);
            if (user_id.equals("null user")) {
                log("No user associated with that number");
                return null;
            }
            return user_id;
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error retrieving user by number");
        }
    }
}
