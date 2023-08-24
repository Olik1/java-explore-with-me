package ru.practicum.main_service.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.dto.CategoriesMapper;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Categories> categoriesList = categoriesRepository.findAll(page).getContent();
        log.info("Запрос GET на получение списка категорий");
        return categoriesList.stream().map(CategoriesMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoriesId(Long catId) {
        Categories categories = getCategoriesIfExist(catId);
        return CategoriesMapper.toCategoryDto(categories);
    }

    @Override
    public CategoryDto createCategories(NewCategoryDto newCategoryDto) {
        Categories categories = categoriesRepository.save(CategoriesMapper.toCategories(newCategoryDto));
        log.info("Запрос POST на сохранение категории: {}", newCategoryDto.getName());
        return CategoriesMapper.toCategoryDto(categories);
    }

    @Override
    public void deleteCategories(Long catId) {
        getCategoriesIfExist(catId);
        categoriesRepository.deleteById(catId);
        log.info("Запрос DELETE на удаление категории: c id: {}", catId);
    }

    @Override
    public CategoryDto updateCategories(CategoryDto categoryDto) {
        Categories categories = getCategoriesIfExist(categoryDto.getId());
        categories.setName(categoryDto.getName());
        log.info("Запрос PATH на изменение категории: c id: {}", categoryDto.getId());
        return CategoriesMapper.toCategoryDto(categoriesRepository.save(categories));
    }

    public Categories getCategoriesIfExist(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдена выбранная категория"));
    }

}
