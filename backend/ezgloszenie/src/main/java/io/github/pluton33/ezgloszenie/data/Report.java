package io.github.pluton33.ezgloszenie.data;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;

public record Report(@Nullable Long id,
                     String title,
                     String description,
                     @Nullable String status,
                     @Nullable Category category,
                     User user,
                     LocalDateTime created_date,
                     LocalDateTime accident_date,
                     String location,
                     boolean userAnonymous
                     ) {
//    public static Report fromEntity(ReportEntity entity) {
//        return new Report(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getUser());
//    }
//    public ReportEntity toEntity() {
//        ReportEntity entity = new ReportEntity();
//        entity.setId(id);
//        entity.setTitle(title);
//        entity.setDescription(description);
//        return entity;
//
//    }
}
