package com.coderarmy.practice.model;

import java.util.Objects;

// Finished for you. No TODOs in this file.
public class Student {
    private Long id;
    private String name;
    private String email;
    private Integer age;   // Integer, not int: age may be NULL in the database

    public Student() {
    }

    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Student(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student s)) return false;
        return Objects.equals(id, s.id) && Objects.equals(name, s.name)
                && Objects.equals(email, s.email) && Objects.equals(age, s.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', email='" + email + "', age=" + age + '}';
    }
}
