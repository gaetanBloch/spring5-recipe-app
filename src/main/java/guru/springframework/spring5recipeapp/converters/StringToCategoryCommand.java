package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Gaetan Bloch
 * Created on 27/03/2020
 */
@RequiredArgsConstructor
@Component
public class StringToCategoryCommand implements Converter<String, CategoryCommand> {
    private final CategoryService categoryService;

    @Override
    public CategoryCommand convert(String id) {
        return categoryService.findById(Long.valueOf(id));
    }
}
