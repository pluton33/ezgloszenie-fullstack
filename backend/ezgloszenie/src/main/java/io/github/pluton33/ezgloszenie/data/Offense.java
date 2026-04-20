package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;

public record Offense(@Nullable Integer id, String title, String content) {
    public static Offense fromEntity(OffenseEntity entity) {
        return new Offense(entity.getId(), entity.getTitle(), entity.getContent());
    }
    public OffenseEntity toEntity() {
        OffenseEntity entity = new OffenseEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setContent(content);
        return entity;

    }
}
