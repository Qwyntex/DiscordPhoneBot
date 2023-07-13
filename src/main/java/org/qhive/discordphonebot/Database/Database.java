package org.qhive.discordphonebot.Database;

import java.sql.*;

public class Database {

    private static Connection connection;
    public static void init() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mariadb://" + System.getenv("DB-URL"),
                System.getenv("DB-USER"),
                System.getenv("DB-ACCESS")
        );

        if (!connection.isValid(1000)) {
            throw new RuntimeException("Can't connect to database");
        }

        connection.prepareStatement("""
                USE DPBot
                """).execute();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Users
                (
                    id   int UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL ,
                    user text                                    NOT NULL
                );
                """).execute();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Numbers
                (
                    number  text,
                    user_id int UNSIGNED NOT NULL,
                    CONSTRAINT `fk_user`
                        FOREIGN KEY (user_id) REFERENCES Users(id)
                            ON DELETE CASCADE
                            ON UPDATE RESTRICT
                );
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

    public static String getUserDbId(String userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT id FROM Users where user = ?
                    """);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return resultSet.getString(1);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e + "\n error getting user");
        }
    }
}
