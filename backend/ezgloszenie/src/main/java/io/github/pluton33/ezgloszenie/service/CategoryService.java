package io.github.pluton33.ezgloszenie.service;

import io.github.pluton33.ezgloszenie.data.CategoriesResponse;
import io.github.pluton33.ezgloszenie.data.Category;
import io.github.pluton33.ezgloszenie.data.CategoryCreationRequest;
import io.github.pluton33.ezgloszenie.data.CategoryEditRequest;

public interface CategoryService {
    CategoriesResponse getCategories();
    Category editCategory(long id,CategoryEditRequest req,String email);
    Category addCategory(CategoryCreationRequest req,String email);
    Category getCategoryById(long id);
    void deleteCategory(long id, String email);
}
