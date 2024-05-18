package ru.mooncess.onlinestore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.repository.CategoryRepository;
import ru.mooncess.onlinestore.service.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testGetAllCategory() {
        List<Category> expectedCategories = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        List<Category> actualCategories = categoryService.getAllCategory();

        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testFindAllCategoriesSortByName() {
        List<Category> expectedCategories = new ArrayList<>();
        when(categoryRepository.findAllCategoriesSortByName()).thenReturn(expectedCategories);

        List<Category> actualCategories = categoryService.findAllCategoriesSortByName();

        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    void testGetCategoryById() {
        Long categoryId = 1L;
        Category expectedCategory = new Category();
        expectedCategory.setId(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        Optional<Category> actualCategory = categoryService.getCategoryById(categoryId);

        assertTrue(actualCategory.isPresent());
        assertEquals(expectedCategory, actualCategory.get());
    }

    @Test
    void testCreateCategory() {
        String categoryName = "Test Category";
        Category newCategory = new Category();
        newCategory.setName(categoryName);

        when(categoryRepository.save(newCategory)).thenReturn(newCategory);

        Optional<Category> createdCategory = categoryService.createCategory(categoryName);

        assertTrue(createdCategory.isPresent());
        assertEquals(newCategory, createdCategory.get());
    }

    @Test
    void testUpdateCategory() {
        Long categoryId = 1L;
        String updatedCategoryName = "Updated Category";

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updatedCategoryName);

        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);

        Optional<Category> resultCategory = categoryService.updateCategory(categoryId, updatedCategoryName);

        assertTrue(resultCategory.isPresent());
        assertEquals(updatedCategory, resultCategory.get());
    }

    @Test
    void testDeleteCategory() {
        Long categoryId = 1L;
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        assertTrue(categoryService.deleteCategory(categoryId));

    }
}