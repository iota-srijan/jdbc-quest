package com.coderarmy.practice.spring;

import com.coderarmy.practice.TestDatabase;
import com.coderarmy.practice.exception.EmailAlreadyExistsException;
import com.coderarmy.practice.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class   SpringStudentRepositoryTest {

    private TestDatabase db;
    private JdbcTemplate jdbcTemplate;
    private SpringStudentRepository repository;

    @BeforeEach
    void setUp() {
        db = new TestDatabase();
        db.resetSchema();
        // In a Boot app this DataSource would be HikariCP, built from application.properties.
        DriverManagerDataSource dataSource = new DriverManagerDataSource(db.url, TestDatabase.USER, TestDatabase.PASSWORD);
        jdbcTemplate = new JdbcTemplate(dataSource);
        repository = new SpringStudentRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("S1a · StudentRowMapper maps every column")
    void mapperMapsColumns() {
        long id = db.insert("Priya", "priya@gmail.com", 22);
        List<Student> rows = jdbcTemplate.query("SELECT id, name, email, age FROM students", new StudentRowMapper());
        assertThat(rows).containsExactly(new Student(id, "Priya", "priya@gmail.com", 22));
    }

    @Test
    @DisplayName("S1b · StudentRowMapper keeps an empty age as null")
    void mapperNullAge() {
        db.insert("Aman", "aman@gmail.com", null);
        Student s = jdbcTemplate.query("SELECT id, name, email, age FROM students", new StudentRowMapper()).get(0);
        assertThat(s.getAge()).as("rs.getInt() turns NULL into 0").isNull();
    }

    @Test
    @DisplayName("S2 · save() inserts with update() and returns true")
    void save() {
        assertThat(repository.save(new Student("Rahul", "rahul@gmail.com", 24))).isTrue();
        assertThat(db.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("S3a · findAll() returns an empty list, never null")
    void findAllEmpty() {
        assertThat(repository.findAll()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("S3b · findAll() returns all students ordered by id")
    void findAllOrdered() {
        long a = db.insert("Rahul", "rahul@gmail.com", 24);
        long b = db.insert("Priya", "priya@gmail.com", 22);
        assertThat(repository.findAll()).containsExactly(
                new Student(a, "Rahul", "rahul@gmail.com", 24),
                new Student(b, "Priya", "priya@gmail.com", 22));
    }

    @Test
    @DisplayName("S4a · findById() returns the student")
    void findById() {
        long id = db.insert("Priya", "priya@gmail.com", 22);
        assertThat(repository.findById(id)).contains(new Student(id, "Priya", "priya@gmail.com", 22));
    }

    @Test
    @DisplayName("S4b · findById() returns Optional.empty() when there is no row")
    void findByIdMissing() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("S4c · findById() lets real database failures propagate")
    void findByIdFailure() {
        db.exec("DROP TABLE students");
        assertThatThrownBy(() -> repository.findById(1L))
                .as("catch only EmptyResultDataAccessException, not every DataAccessException")
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("S5a · update() returns true for an existing id and changes the row")
    void update() {
        long id = db.insert("Rahul", "rahul@gmail.com", 24);
        assertThat(repository.update(id, new Student("Rahul Sharma", "rahul.sharma@gmail.com", 25))).isTrue();
        assertThat(db.row(id)).containsExactly("Rahul Sharma", "rahul.sharma@gmail.com", 25);
    }

    @Test
    @DisplayName("S5b · update() returns false for a missing id")
    void updateMissing() {
        assertThat(repository.update(999L, new Student("X", "x@x.com", 1))).isFalse();
    }

    @Test
    @DisplayName("S5c · deleteById() returns true, then false")
    void delete() {
        long id = db.insert("Rahul", "rahul@gmail.com", 24);
        assertThat(repository.deleteById(id)).isTrue();
        assertThat(repository.deleteById(id)).isFalse();
    }

    @Test
    @DisplayName("S6 · save() with a taken email throws EmailAlreadyExistsException")
    void saveDuplicate() {
        db.insert("Rahul", "rahul@gmail.com", 24);
        assertThatThrownBy(() -> repository.save(new Student("Another Rahul", "rahul@gmail.com", 30)))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("rahul@gmail.com");
    }

    @Test
    @DisplayName("S7 · countByMinAge() counts students at or above an age")
    void countByMinAge() {
        db.insert("Rahul", "rahul@gmail.com", 24);
        db.insert("Priya", "priya@gmail.com", 22);
        db.insert("Aman", "aman@gmail.com", 27);
        db.insert("Zoya", "zoya@gmail.com", null);
        assertThat(repository.countByMinAge(24)).isEqualTo(2);
        assertThat(repository.countByMinAge(100)).isZero();
    }
}
