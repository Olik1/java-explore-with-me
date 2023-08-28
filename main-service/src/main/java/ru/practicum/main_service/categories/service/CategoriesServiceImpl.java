package ru.practicum.main_service.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.categories.dto.CategoriesMapper;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.model.Categories;
import ru.practicum.main_service.categories.repository.CategoriesRepository;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;


    @Override
    @Transactional(readOnly = true)
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
        if (categoriesRepository.existsCategoriesByName(newCategoryDto.getName())) {
            throw new ConflictException("Такая категория уже есть");
        }
        Categories categories = categoriesRepository.save(CategoriesMapper.toCategories(newCategoryDto));
        log.info("Запрос POST на сохранение категории: {}", newCategoryDto.getName());
        return CategoriesMapper.toCategoryDto(categories);
    }

    @Override
    public void deleteCategories(Long catId) {


        var category = categoriesRepository.findById(catId);

        if (category == null) {
            throw new ObjectNotFoundException("Не найдена выбранная категория");
        }

        if (eventRepository.existsEventsByCategory_Id(catId)) {
            throw new ConflictException("Такой пользователь уже есть");
        }
        categoriesRepository.deleteById(catId);
        log.info("Запрос DELETE на удаление категории: c id: {}", catId);
    }

    @Override
    public CategoryDto updateCategories(CategoryDto categoryDto) {


        Categories categories = getCategoriesIfExist(categoryDto.getId());

        if (categoriesRepository.existsCategoriesByNameAndIdNot(categoryDto.getName(), categoryDto.getId())) {
            throw new ConflictException("Такая категория уже есть");
        }

        categories.setName(categoryDto.getName());
        log.info("Запрос PATH на изменение категории: c id: {}", categoryDto.getId());
        return CategoriesMapper.toCategoryDto(categoriesRepository.save(categories));
    }

    private Categories getCategoriesIfExist(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдена выбранная категория"));
    }

}
