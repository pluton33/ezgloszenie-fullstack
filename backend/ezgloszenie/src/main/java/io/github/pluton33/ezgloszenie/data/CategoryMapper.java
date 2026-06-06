package io.github.pluton33.ezgloszenie.data;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toDto(CategoryEntity entity);
    CategoryEntity toEntity(Category category);
}
