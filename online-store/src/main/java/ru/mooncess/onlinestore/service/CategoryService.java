package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllCategoriesSortByName() {
        return categoryRepository.findAllCategoriesSortByName();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> createCategory(String nameOfCategory) {
        try {
            Category newCategory = new Category();
            newCategory.setName(nameOfCategory);
            return Optional.of(categoryRepository.save(newCategory));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Category> updateCategory(Long id, String nameOfCategory) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            try {
                Category updatedCategory = new Category();
                updatedCategory.setId(id);
                updatedCategory.setName(nameOfCategory);
                return Optional.of(categoryRepository.save(updatedCategory));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
