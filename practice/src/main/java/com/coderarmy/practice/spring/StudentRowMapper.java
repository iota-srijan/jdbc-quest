package com.coderarmy.practice.spring;

import com.coderarmy.practice.model.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * S1 · RowMapper. One row in, one Student out.
 * Spring calls this once per row. You never call next() here.
 * Hint: keep an empty (NULL) age as null.
 */
public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        throw new UnsupportedOperationException("TODO S1: mapRow()");
    }
}
