package ru.practicum.main_service.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.service.CategoriesService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    private final CategoriesService categoriesService;
    @PostMapping
    public CategoryDto createCategories(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoriesService.createCategories(newCategoryDto);
    }
    @DeleteMapping("/{catId}")
    public void deleteCategories (@PathVariable Long catId) {
        categoriesService.deleteCategories(catId);
    }
    @PatchMapping ("/{catId}")
    public CategoryDto updateCategories(@RequestBody CategoryDto categoryDto) {
        return categoriesService.updateCategories(categoryDto);
    }

}
