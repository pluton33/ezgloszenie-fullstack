package io.github.pluton33.ezgloszenie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.pluton33.ezgloszenie.data.CategoriesResponse;
import io.github.pluton33.ezgloszenie.data.Category;
import io.github.pluton33.ezgloszenie.data.CategoryCreationRequest;
import io.github.pluton33.ezgloszenie.data.CategoryEditRequest;
import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;
import io.github.pluton33.ezgloszenie.service.CategoryService;
import io.github.pluton33.ezgloszenie.service.ReportService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
public class CategoryController {
    @Autowired
    private CategoryService service;
    @GetMapping("/categories")
    public CategoriesResponse getCategories() {
        return service.getCategories();
    }
    @GetMapping("/categories/{id}")
    public Category getCategoryById(@PathVariable long id) {
        return service.getCategoryById(id);
    }
    @PutMapping("/addCategory")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Category addCategory(@RequestBody CategoryCreationRequest req, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        return service.addCategory(req, email);
    }
    @PostMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Category editCategory(@PathVariable long id,@RequestBody CategoryEditRequest category, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        return service.editCategory(id,category, email);
    }
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        service.deleteCategory(id, email);
        return ResponseEntity.noContent().build();
    }
}
