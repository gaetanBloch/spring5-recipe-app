package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.converters.CategoryToCategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import guru.springframework.spring5recipeapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Gaetan Bloch
 * Created on 27/03/2020
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryCommand categoryToCategoryCommand;

    @Override
    public Set<CategoryCommand> findAll() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(categoryToCategoryCommand::convert)
                .collect(Collectors.toSet());
    }

    @Override
    public CategoryCommand findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            log.error("No category found for id = " + id);
            throw new RuntimeException("No category found for id = " + id);
        }
        return categoryToCategoryCommand.convert(category.get());
    }
}
