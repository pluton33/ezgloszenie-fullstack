package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record Status (@Nullable Long id, String name, LocalDateTime validFrom, @Nullable LocalDateTime validTo){

}
