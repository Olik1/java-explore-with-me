package ru.practicum.main_service.categories.service;

import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoriesId(Long catId);

    CategoryDto createCategories(NewCategoryDto newCategoryDto);

    void deleteCategories(Long catId);

    CategoryDto updateCategories(CategoryDto categoryDto);

}
