package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.CategoriesResponse;
import io.github.pluton33.ezgloszenie.data.Category;

public interface CategoryService {
    CategoriesResponse getCategories();
    Category editCategory(long id,Category category,String email);
    Category addCategory(Category category,String email);
    Category getCategoryById(long id);
    void deleteCategory(long id, String email);
}
