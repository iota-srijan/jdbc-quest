package com.coderarmy.practice.raw;

import com.coderarmy.practice.TestDatabase;
import com.coderarmy.practice.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class RawStudentRepositoryTest {

    private TestDatabase db;
    private RawStudentRepository repository;

    @BeforeEach
    void setUp() {
        db = new TestDatabase();
        db.resetSchema();
        repository = new RawStudentRepository(db.url, TestDatabase.USER, TestDatabase.PASSWORD);
    }

    @Test
    @DisplayName("R1a · save() inserts the student and returns 1")
    void saveInserts() {
        int rows = repository.save(new Student("Rahul", "rahul@gmail.com", 24));
        assertThat(rows).as("affected-row count").isEqualTo(1);
        assertThat(db.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("R1b · save() stores a null age as NULL")
    void saveNullAge() {
        repository.save(new Student("Aman", "aman@gmail.com", null));
        long id = db.insert("Other", "other@gmail.com", 1) - 1;
        assertThat(db.row(id)).containsExactly("Aman", "aman@gmail.com", null);
    }

    @Test
    @DisplayName("R2 · saveAndReturnId() returns the generated id")
    void saveReturnsId() {
        long first = repository.saveAndReturnId(new Student("Rahul", "rahul@gmail.com", 24));
        long second = repository.saveAndReturnId(new Student("Priya", "priya@gmail.com", 22));
        assertThat(second).isEqualTo(first + 1);
        assertThat(db.row(second)).containsExactly("Priya", "priya@gmail.com", 22);
    }

    @Test
    @DisplayName("R3+R4a · findById() returns the full student, id included")
    void findByIdFound() {
        long id = db.insert("Priya", "priya@gmail.com", 22);
        assertThat(repository.findById  (id)).contains(new Student(id, "Priya", "priya@gmail.com", 22));
    }

    @Test
    @DisplayName("R3+R4b · an empty age comes back as null, not 0")
    void nullAgeStaysNull() {
        long id = db.insert("Aman", "aman@gmail.com", null);
        Student s = repository.findById(id).orElseThrow();
        assertThat(s.getAge()).as("getInt() would give 0 here; use getObject(\"age\", Integer.class)").isNull();
    }

    @Test
    @DisplayName("R4c · findById() returns Optional.empty() for a missing id")
    void findByIdMissing() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("R4d · findById() does NOT hide a database failure as 'not found'")
    void findByIdFailure() {
        db.exec("DROP TABLE students");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> repository.findById(1L));
        assertThat(ex).isNotInstanceOf(UnsupportedOperationException.class);
        assertThat(ex.getCause()).as("wrap the SQLException: new RuntimeException(message, e)").isInstanceOf(SQLException.class);
    }

    @Test
    @DisplayName("R5a · findAll() returns an empty list for an empty table")
    void findAllEmpty() {
        assertThat(repository.findAll()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("R5b · findAll() returns every student ordered by id")
    void findAllOrdered() {
        long a = db.insert("Rahul", "rahul@gmail.com", 24);
        long b = db.insert("Priya", "priya@gmail.com", 22);
        long c = db.insert("Aman", "aman@gmail.com", null);
        assertThat(repository.findAll()).containsExactly(
                new Student(a, "Rahul", "rahul@gmail.com", 24),
                new Student(b, "Priya", "priya@gmail.com", 22),
                new Student(c, "Aman", "aman@gmail.com", null));
    }

    @Test
    @DisplayName("R6a · update() changes the row and returns 1")
    void updateExisting() {
        long id = db.insert("Rahul", "rahul@gmail.com", 24);
        int rows = repository.update(id, new Student("Rahul Sharma", "rahul.sharma@gmail.com", 25));
        assertThat(rows).isEqualTo(1);
        assertThat(db.row(id)).containsExactly("Rahul Sharma", "rahul.sharma@gmail.com", 25);
    }

    @Test
    @DisplayName("R6b · update() returns 0 for a missing id")
    void updateMissing() {
        assertThat(repository.update(999L, new Student("X", "x@x.com", 1))).isZero();
    }

    @Test
    @DisplayName("R6c · deleteById() returns 1, then 0 the second time")
    void delete() {
        long id = db.insert("Rahul", "rahul@gmail.com", 24);
        assertThat(repository.deleteById(id)).isEqualTo(1);
        assertThat(repository.deleteById(id)).isZero();
        assertThat(db.count()).isZero();
    }

    @Test
    @DisplayName("R7a · findByEmail() finds a real email")
    void findByEmail() {
        long id = db.insert("Priya", "priya@gmail.com", 22);
        assertThat(repository.findByEmail("priya@gmail.com")).contains(new Student(id, "Priya", "priya@gmail.com", 22));
    }

    @Test
    @DisplayName("R7b · findByEmail() is not fooled by SQL injection")
    void findByEmailInjection() {
        db.insert("Rahul", "rahul@gmail.com", 24);
        db.insert("Priya", "priya@gmail.com", 22);
        assertThat(repository.findByEmail("' OR '1'='1")).as("glued SQL would match every row").isEmpty();
        assertThat(repository.findByEmail("x' OR 'a'='a")).isEmpty();
    }

    @Test
    @DisplayName("R8a · findAllSortedBy() sorts by an allowed column")
    void sortAllowed() {
        db.insert("Rahul", "rahul@gmail.com", 24);
        db.insert("Aman", "aman@gmail.com", 27);
        db.insert("Priya", "priya@gmail.com", 22);
        List<String> byName = repository.findAllSortedBy("name").stream().map(Student::getName).toList();
        List<Integer> byAge = repository.findAllSortedBy("age").stream().map(Student::getAge).toList();
        assertThat(byName).containsExactly("Aman", "Priya", "Rahul");
        assertThat(byAge).containsExactly(22, 24, 27);
    }

    @Test
    @DisplayName("R8b · findAllSortedBy() rejects anything not on the allow-list")
    void sortRejected() {
        db.insert("Rahul", "rahul@gmail.com", 24);
        assertThatThrownBy(() -> repository.findAllSortedBy("name; DROP TABLE students"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> repository.findAllSortedBy("email"))
                .as("only id, name and age are allowed")
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(db.count()).as("table must still exist").isEqualTo(1);
    }
}
