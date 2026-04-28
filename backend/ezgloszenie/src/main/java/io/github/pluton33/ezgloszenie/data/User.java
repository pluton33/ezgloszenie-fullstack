package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Enumerated;

import java.util.Date;

public record User(
        @Nullable Integer  id,
        UserRole role,
        String email,
        String passwordHash,
        Integer badgeNumber,
        String firstName,
        String lastName,
        Date createdAt
) {
}