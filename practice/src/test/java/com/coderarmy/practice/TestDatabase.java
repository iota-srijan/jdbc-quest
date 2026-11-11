package com.coderarmy.practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/** Test helper: a fresh in-memory database (H2 in MySQL mode) and direct SQL helpers. */
public final class TestDatabase {
    public static final String USER = "sa";
    public static final String PASSWORD = "";
    public final String url = "jdbc:h2:mem:practice_" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1";

    public void resetSchema() {
        exec("DROP TABLE IF EXISTS students");
        exec("""
                CREATE TABLE students (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    age INT
                )""");
    }

    public void exec(String sql) {
        try (Connection c = DriverManager.getConnection(url, USER, PASSWORD); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Inserts directly (without your code) and returns the new id. */
    public long insert(String name, String email, Integer age) {
        try (Connection c = DriverManager.getConnection(url, USER, PASSWORD);
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO students(name, email, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setObject(3, age);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {
        try (Connection c = DriverManager.getConnection(url, USER, PASSWORD);
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM students")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] row(long id) {
        try (Connection c = DriverManager.getConnection(url, USER, PASSWORD);
             PreparedStatement ps = c.prepareStatement("SELECT name, email, age FROM students WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Object[]{rs.getString(1), rs.getString(2), rs.getObject(3, Integer.class)};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
