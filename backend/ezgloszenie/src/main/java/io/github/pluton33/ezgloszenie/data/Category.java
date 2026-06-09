package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;

public record Category(
@Nullable Long id,
String name
) {
    
}
