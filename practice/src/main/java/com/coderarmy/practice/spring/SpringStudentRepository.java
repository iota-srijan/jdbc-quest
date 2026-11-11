package com.coderarmy.practice.spring;

import com.coderarmy.practice.exception.EmailAlreadyExistsException;
import com.coderarmy.practice.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

/**
 * CHAPTER 2 · SPRING JDBC QUESTS (S1–S7)
 *
 * Same students table as chapter 1. This time: no Connection, no PreparedStatement,
 * no try-with-resources, no SQLException. JdbcTemplate does all of that.
 *
 * Run only this chapter's tests:   mvn test -Dtest=SpringStudentRepositoryTest
 */
public class SpringStudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper = new StudentRowMapper();  // reuse one mapper

    public SpringStudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * S2 · Create with update(). Return true when exactly one row was inserted.
     * S6 · If the email is already taken, throw EmailAlreadyExistsException instead.
     *      Hint: Spring throws DuplicateKeyException for a UNIQUE violation.
     */
    public boolean save(Student student) {
        throw new UnsupportedOperationException("TODO S2: save()");
    }

    /**
     * S3 · Read all with query(), ordered by id. Empty list when nothing matches.
     */
    public List<Student> findAll() {
        throw new UnsupportedOperationException("TODO S3: findAll()");
    }

    /**
     * S4 · Read one with queryForObject().
     * Zero rows → Optional.empty(). Any OTHER database failure must keep propagating.
     */
    public Optional<Student> findById(Long id) {
        throw new UnsupportedOperationException("TODO S4: findById()");
    }

    /**
     * S5 · Update with update(). True when one row changed, false when no such id.
     */
    public boolean update(Long id, Student student) {
        throw new UnsupportedOperationException("TODO S5: update()");
    }

    /**
     * S5 · Delete with update(). True when one row was deleted.
     */
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("TODO S5: deleteById()");
    }

    /**
     * S7 · Count students with age >= minAge.
     * Hint: queryForObject(sql, Integer.class, minAge) maps a single number, no RowMapper needed.
     */
    public int countByMinAge(int minAge) {
        throw new UnsupportedOperationException("TODO S7: countByMinAge()");
    }
}
