package io.github.pluton33.ezgloszenie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.pluton33.ezgloszenie.data.CategoriesResponse;
import io.github.pluton33.ezgloszenie.data.Category;
import io.github.pluton33.ezgloszenie.data.CategoryCreationRequest;
import io.github.pluton33.ezgloszenie.data.CategoryEntity;
import io.github.pluton33.ezgloszenie.data.CategoryMapper;
import io.github.pluton33.ezgloszenie.data.UserEntity;
import io.github.pluton33.ezgloszenie.data.UserRole;
import io.github.pluton33.ezgloszenie.repository.CategoriesRepository;
import io.github.pluton33.ezgloszenie.repository.UsersRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final UserRole minimumPermissionRole = UserRole.ADMIN;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final CategoryMapper categoryMapper;
    public CategoryServiceImpl(CategoriesRepository categoriesRepository,UsersRepository usersRepository,CategoryMapper categoryMapper)
    {
        this.categoriesRepository = categoriesRepository;
        this.usersRepository = usersRepository;
        this.categoryMapper = categoryMapper;
    }
    @Override
    public CategoriesResponse getCategories() {
        List<CategoryEntity> categoryEntities = categoriesRepository.findAll();
        List<Category> categories = new ArrayList<Category>();
        for(CategoryEntity catE : categoryEntities)
            categories.add(categoryMapper.toDto(catE));
        return new CategoriesResponse(categories);
    }
    @Transactional
    @Override
    public Category editCategory(long id, Category category, String email) {
        //Sprawdzanie uprawnień
        UserEntity userEntity = usersRepository.findByEmail(email)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this email is not existing!"));

        if(userEntity.getRole().getPermissionLevel()<minimumPermissionRole.getPermissionLevel())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have priveleges to edit that!");
        if(category.id()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Category ID is required");
        if(id != category.id()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Category ID mismatch");
        }
        categoriesRepository.findById(id)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found!"));
        CategoryEntity categoryEntity = categoryMapper.toEntity(category);
        CategoryEntity editedEntity = categoriesRepository.save(categoryEntity);
        return categoryMapper.toDto(editedEntity);
    }
    @Override
    public Category addCategory(CategoryCreationRequest req, String email) {

        UserEntity userEntity = usersRepository.findByEmail(email)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this email is not existing!"));

        if(userEntity.getRole().getPermissionLevel()<minimumPermissionRole.getPermissionLevel())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have priveleges to add that!");
        Category category = new Category(null, req.name());
        CategoryEntity categoryEntity = categoryMapper.toEntity(category);
        CategoryEntity addedEntity = categoriesRepository.save(categoryEntity);
        return categoryMapper.toDto(addedEntity);
    }
    @Override
    public Category getCategoryById(long id) {
        CategoryEntity categoryEntity = categoriesRepository.findById(id)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found!"));
        return categoryMapper.toDto(categoryEntity);
    }
    @Override
    public void deleteCategory(long id, String email) {
        UserEntity userEntity = usersRepository.findByEmail(email)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this email is not existing!"));

        if(userEntity.getRole().getPermissionLevel()<minimumPermissionRole.getPermissionLevel())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have priveleges to edit that!");
        
        CategoryEntity categoryEntity = categoriesRepository.findById(id)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found!"));
        categoriesRepository.delete(categoryEntity);
    }
    
}
