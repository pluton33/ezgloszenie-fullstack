package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;


public record User(
        @Nullable Long  id,
        UserRole role,
        String email,
        Integer badgeNumber,
        String firstName,
        String lastName
) {
}