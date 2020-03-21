package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {
    @Synchronized
    @Nullable
    @Override
    public CategoryCommand convert(@Nullable Category category) {
        if (category == null) {
            return null;
        }

        return CategoryCommand.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
