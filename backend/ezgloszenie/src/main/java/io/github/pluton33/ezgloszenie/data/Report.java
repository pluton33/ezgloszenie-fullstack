package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;

public record Report(@Nullable Integer id, String title, String content) {
    public static Report fromEntity(ReportEntity entity) {
        return new Report(entity.getId(), entity.getTitle(), entity.getContent());
    }
    public ReportEntity toEntity() {
        ReportEntity entity = new ReportEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setContent(content);
        return entity;

    }
}
