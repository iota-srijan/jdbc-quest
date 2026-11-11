# JDBC practice quests

Local coding quests for the JDBC course page. You write real Java, and JUnit tests check it,
the same way a "submit" button would.

The tests use **H2**, an in-memory database running in MySQL mode, so you don't need MySQL running.

## Open and run

**IntelliJ:** File → Open → pick this `jdbc-practice` folder → right-click `src/test/java` → *Run 'All Tests'*.
Or click the green ▶ next to any single test.

**Terminal** (needs Java 21+ and Maven on your PATH):

```bash
mvn test                                   # everything
mvn test -Dtest=RawStudentRepositoryTest   # chapter 1 only
mvn test -Dtest=SpringStudentRepositoryTest  # chapter 2 only
```

Right now every test fails. That's expected: each method throws `UnsupportedOperationException("TODO ...")`.
Turn them green one quest at a time.

## Quests

| Quest | File | Method | Tests |
|---|---|---|---|
| R1 | `raw/RawStudentRepository` | `save()` | R1a, R1b |
| R2 | | `saveAndReturnId()` | R2 |
| R3 | | `mapRow()` | needed by R3+R4, R5, R7 |
| R4 | | `findById()` | R3+R4a–d |
| R5 | | `findAll()` | R5a, R5b |
| R6 | | `update()`, `deleteById()` | R6a–c |
| R7 | | `findByEmail()` (injection-proof) | R7a, R7b |
| R8 | | `findAllSortedBy()` (allow-list) | R8a, R8b |
| S1 | `spring/StudentRowMapper` | `mapRow()` | S1a, S1b |
| S2 | `spring/SpringStudentRepository` | `save()` | S2 |
| S3 | | `findAll()` | S3a, S3b |
| S4 | | `findById()` | S4a–c |
| S5 | | `update()`, `deleteById()` | S5a–c |
| S6 | | `save()` duplicate email | S6 |
| S7 | | `countByMinAge()` | S7 |

Do R1–R8 first. Chapter 2 is more satisfying once you've felt the chapter 1 boilerplate.

## Rules the tests enforce

- Use `?` placeholders. R7 feeds in `' OR '1'='1` and expects no match.
- An empty age must come back as `null`, not `0`. Use `getObject("age", Integer.class)`.
- "Not found" is `Optional.empty()`. A broken database must throw. R4d and S4c drop the table to check this.
- Raw JDBC: wrap `SQLException` as `new RuntimeException("message", e)`. Keep the cause.

## Later: point it at your real MySQL

Add `mysql-connector-j` (runtime scope) to `pom.xml` and create the repository with
`jdbc:mysql://localhost:3306/student_db`, your username and password. The repository code stays the same.
