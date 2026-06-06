package io.github.pluton33.ezgloszenie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.pluton33.ezgloszenie.data.CategoryEntity;

public interface CategoriesRepository extends JpaRepository<CategoryEntity, Long> {
    
}
