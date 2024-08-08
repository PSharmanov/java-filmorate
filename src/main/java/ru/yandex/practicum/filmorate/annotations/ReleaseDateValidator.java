package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {

    @Override
    public void initialize(ValidReleaseDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate releaseData, ConstraintValidatorContext context) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        return releaseData.isAfter(minReleaseDate);
    }
}
