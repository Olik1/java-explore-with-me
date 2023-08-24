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

    //checked
    @PostMapping
    public CategoryDto createCategories(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoriesService.createCategories(newCategoryDto);
    }
    //checked
    @DeleteMapping("/{catId}")
    public void deleteCategories(@PathVariable Long catId) {
        categoriesService.deleteCategories(catId);
    }
    //checked
    @PatchMapping("/{catId}")
    public CategoryDto updateCategories(@PathVariable Long catId,
                                        @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(catId);
        var s = categoriesService.updateCategories(categoryDto);
        return s;
    }

}
