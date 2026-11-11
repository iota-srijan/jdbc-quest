# JDBC Quest

Learn JDBC by playing it. Start with raw JDBC (`Connection`, `PreparedStatement`, `ResultSet`), then move to Spring's `JdbcTemplate`.

It's a small learning kit with three parts:

| Part | What it is | Where |
|---|---|---|
| **The course** | A game-like course in your browser: short lessons, a code editor, quizzes, XP and levels. SQL lessons run on a real SQLite database inside the page. | `site/jdbc-quest.html` |
| **Two explainer labs** | Interactive pages with animations that explain the ideas the course uses. The course links into them. | `site/jdbc-fundamentals.html`, `site/spring-jdbc.html` |
| **A Java practice project** | Real Java (Maven + JUnit). You fill in empty methods and the tests tell you when you're right. No MySQL needed. | `practice/` |

## Quick start

**Play the course.** Open `site/jdbc-quest.html` in a browser (double-click it). The lab pages sit next to it, so the "Deep dive" links work. The page loads a few libraries from CDNs, so you need an internet connection.

```bash
# or serve the folder locally
cd site
python -m http.server 8000      # then open http://localhost:8000/jdbc-quest.html
```

Your progress, XP and code are saved in your browser (`localStorage`). They don't leave your machine. The sidebar has "Unlock all lessons (review mode)" and a "Restore my progress" box if you switch browsers.

**Run the practice project.** You need Java 21+ and Maven.

```bash
cd practice
mvn test                                     # everything (starts out all red)
mvn test -Dtest=RawStudentRepositoryTest     # chapter 1 only
mvn test -Dtest=SpringStudentRepositoryTest  # chapter 2 only
```

Every method starts as `throw new UnsupportedOperationException("TODO ...")`. Turn the tests green one quest at a time. The tests use H2, an in-memory database in MySQL mode, so nothing has to be installed. See [`practice/README.md`](practice/README.md) for the quest table and the rules the tests enforce.

## The learning path

25 lessons in two chapters. Each chapter ends with a local quest (the Java project) and a boss quiz.

| Chapter | You learn |
|---|---|
| **1 · Raw JDBC** (13 lessons) | Why Java needs a driver, the JDBC URL, `SELECT` and `INSERT/UPDATE`, which `execute` method to use, walking a `ResultSet`, the NULL trap, SQL injection (you attack it, then fix it), placeholders in the right order, closing resources. Local quests **R1 to R8**. |
| **2 · Spring JDBC** (12 lessons) | `DataSource` and connection pools, configuring Spring Boot, `JdbcTemplate.update`, `RowMapper`, `query` vs `queryForObject`, `Optional` for "not found", counting with `Integer.class`, Spring's exception hierarchy. Local quests **S1 to S7**. |

Suggested order: play chapter 1 and do **R1 to R8**, then play chapter 2 and do **S1 to S7**. Chapter 2 is more satisfying once you've felt the chapter 1 boilerplate.

## Fork it and learn JDBC your way

The course is just files. There's no framework and no build step, so you can change it and fill it with whatever you're learning from: your lecture notes, a video series, the official docs, a book.

1. **Fork and clone** this repo.
2. **Pick your sources.** Write them down in the table at the bottom of this file, so you can see what you're pulling from.
3. **Turn a source into a lab page.** Copy `site/jdbc-fundamentals.html` and replace its content. Give each section an `id` (for example `id="journey"`), so lessons can link straight to it.
4. **Turn the lab into lessons.** Open `site/jdbc-quest.html` and find the `COURSE DATA` block. Lessons are plain objects in the `COURSE` list:

   ```js
   { id:'1-1', title:'Two programs, one translator', kind:'quiz', xp:10,
     body:`<p>Explain the idea here. HTML is fine.</p>
           <p class="deep">Deep dive: <a href="${LAB1}#journey">the journey of one query</a>.</p>`,
     qs:[ {q:'Your question?', o:['wrong','right','wrong','wrong'], a:1, ex:'Why the answer is right.'} ] },
   ```

   A coding lesson (`kind:'java'`) has a `starter` code string and a list of `tests`. Each test has a name `n`, a check `t` (a regular expression on the learner's code) and a hint `h`:

   ```js
   { id:'1-2', title:'Dial the database', kind:'java', xp:25, file:'Main.java',
     task:{ t:'Connect to student_db', items:['Set the URL…','Open the connection…'] },
     starter:`public class Main { /* TODO */ }`,
     tests:[ {n:'URL points at localhost:3306/student_db', t:has(/URL ?= ?"jdbc:mysql:\/\/localhost:3306\/student_db"/), h:'Format: "jdbc:mysql://HOST:PORT/DATABASE".'} ] },
   ```

   The other kinds are `sql`, `attack`, `local` and `boss`. Search the file for `kind:'sql'`, `kind:'attack'`, and so on to copy a working example.
5. **Change the data.** The `SEED` constant holds the tables and rows that the SQL lessons run on. Replace it with your own.
6. **Change the progress key.** `const KEY = 'jdbc-quest-v1'` is where your browser stores progress. Rename it in your fork so it doesn't mix with the original.
7. **Change the practice project.** Add or remove quests in `practice/src/main/java`, and write the matching tests in `practice/src/test/java`. A quest is a method that throws `UnsupportedOperationException` plus a test that fails until it's implemented.

Keep the loop short: read a source, write one lab section, write three lessons from it, and add one test you have to make pass.

## Repo layout

```
site/
  jdbc-quest.html          the course (all lessons live in one file)
  jdbc-fundamentals.html   lab 1: raw JDBC, the journey of a query
  spring-jdbc.html         lab 2: Spring JDBC, DataSource, JdbcTemplate
practice/
  pom.xml                  Spring Boot 4.1 parent, Java 21, H2, JUnit 5, AssertJ
  src/main/java/...        the stubs you fill in (raw/ and spring/)
  src/test/java/...        the tests that check you
```

## My sources

Fill this in as you learn, so you remember where each idea came from.

| Topic | Source | Link or note |
|---|---|---|
| | | |
| | | |

Built while working through the Coder Army Spring course, plus the Java and Spring documentation.
