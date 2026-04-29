package io.github.pluton33.ezgloszenie.data;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    Report toDto(ReportEntity entity);
    ReportEntity toEntity(Report user);
}
