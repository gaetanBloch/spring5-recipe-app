package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Gaetan Bloch
 * Created on 27/03/2020
 */
@Component
public class CategoryCommandToString implements Converter<CategoryCommand, String> {
    @Override
    public String convert(CategoryCommand categoryCommand) {
        return categoryCommand.getId().toString();
    }
}
