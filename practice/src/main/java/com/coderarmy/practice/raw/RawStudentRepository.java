package com.coderarmy.practice.raw;

import com.coderarmy.practice.model.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * CHAPTER 1 · RAW JDBC QUESTS (R1–R8)
 *
 * Table (already created by the tests):
 *   students(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL,
 *            email VARCHAR(150) NOT NULL UNIQUE, age INT)
 *
 * Rules for every quest:
 *   - Use DriverManager.getConnection(url, username, password) to dial.
 *   - Use PreparedStatement with ? placeholders. Never glue values into SQL.
 *   - Use try-with-resources so everything closes in reverse order.
 *   - Catch SQLException and rethrow it wrapped in a RuntimeException. Never swallow it.
 *
 * Run only this chapter's tests:   mvn test -Dtest=RawStudentRepositoryTest
 */
public class RawStudentRepository {

    private final String url;
    private final String username;
    private final String password;

    public RawStudentRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * R1 · Create. Insert the student and return the affected-row count (1 when saved).
     * Hint: three ? blanks, setString / setString / setObject (age may be null).
     */
    public int save(Student student) {
        throw new UnsupportedOperationException("TODO R1: save()");
    }

    /**
     * R2 · Create + get id. Insert the student and return the id MySQL generated.
     * Hint: connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), then getGeneratedKeys().
     */
    public long saveAndReturnId(Student student) {
        throw new UnsupportedOperationException("TODO R2: saveAndReturnId()");
    }

    /**
     * R3 · Map one row. Turn the ResultSet's CURRENT row into a Student.
     * Hint: an empty (NULL) age must stay null, not become 0.
     */
    private Student mapRow(ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException("TODO R3: mapRow()");
    }

    /**
     * R4 · Read one. Optional.empty() when no row matches.
     * A broken database (SQLException) must NOT turn into Optional.empty(): rethrow it.
     */
    public Optional<Student> findById(Long id) {
        throw new UnsupportedOperationException("TODO R4: findById()");
    }

    /**
     * R5 · Read all, ordered by id. Empty list when the table is empty.
     * Hint: while (resultSet.next()) { ... }
     */
    public List<Student> findAll() {
        throw new UnsupportedOperationException("TODO R5: findAll()");
    }

    /**
     * R6 · Update name, email and age for the given id. Return the affected-row count.
     * Hint: four ? blanks. The id is the 4th one.
     */
    public int update(Long id, Student student) {
        throw new UnsupportedOperationException("TODO R6: update()");
    }

    /**
     * R6 · Delete by id. Return the affected-row count (0 when nothing matched).
     */
    public int deleteById(Long id) {
        throw new UnsupportedOperationException("TODO R6: deleteById()");
    }

    /**
     * R7 · Injection-proof lookup. The tests will pass in attacker input like  ' OR '1'='1
     * and expect an empty result, not every student.
     */
    public Optional<Student> findByEmail(String email) {
        throw new UnsupportedOperationException("TODO R7: findByEmail()");
    }

    /**
     * R8 · Sort by a column the caller chooses: "id", "name" or "age" only.
     * ? can't stand for a column name, so check the input against an allow-list
     * and throw IllegalArgumentException for anything else.
     */
    public List<Student> findAllSortedBy(String column) {
        throw new UnsupportedOperationException("TODO R8: findAllSortedBy()");
    }
}
