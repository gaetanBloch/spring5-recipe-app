package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;

import java.util.Set;

/**
 * @author Gaetan Bloch
 * Created on 27/03/2020
 */
public interface CategoryService {
    Set<CategoryCommand> findAll();

    CategoryCommand findById(Long id);
}
