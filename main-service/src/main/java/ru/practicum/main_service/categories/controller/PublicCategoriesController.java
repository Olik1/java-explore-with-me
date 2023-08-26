package ru.practicum.main_service.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.service.CategoriesService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoriesController {
    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10")
            @PositiveOrZero Integer size) {
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoriesId(@PathVariable Long catId) {
        return categoriesService.getCategoriesId(catId);
    }

}
