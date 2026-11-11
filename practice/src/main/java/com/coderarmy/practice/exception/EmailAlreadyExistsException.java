package com.coderarmy.practice.exception;

// Finished for you. Throw this from SpringStudentRepository.save() when the email is taken.
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("A student with email " + email + " already exists");
    }
}
