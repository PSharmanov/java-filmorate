package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUser() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("user_login");
        user.setName("User Name");
        user.setBirthday(LocalDate.of(2024, 1, 1));
        user.setRegistrationDate(Instant.now());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Обнаружены некорректные данные: " + violations);
    }

    @Test
    void emailIsEmpty() {
        User user = new User();
        user.setEmail("");
        user.setLogin("user_login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Электронная почта не может быть пустой", violations.iterator().next().getMessage());
    }

    @Test
    void emailIsInvalidFormat() {
        User user = new User();
        user.setEmail("user yandex.ru");
        user.setLogin("user_login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Электронная почта должна иметь формат: login@yandex.ru", violations.iterator().next().getMessage());
    }

    @Test
    void loginIsEmpty() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void loginContainsSpace() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("user login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    void birthdayIsInFuture() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("user_login");
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем.", violations.iterator().next().getMessage());
    }

    @Test
    void birthdayIsToday() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("user_login");
        user.setBirthday(LocalDate.now());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Expected no violations, but found: " + violations);
    }

}