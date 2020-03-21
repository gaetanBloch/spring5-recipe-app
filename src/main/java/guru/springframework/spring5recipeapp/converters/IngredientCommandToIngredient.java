package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Component
@RequiredArgsConstructor
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    @Nullable
    @Synchronized
    @Override
    public Ingredient convert(@Nullable IngredientCommand ingredientCommand) {
        if (ingredientCommand == null) {
            return null;
        }

        return Ingredient.builder()
                .amount(ingredientCommand.getAmount())
                .description(ingredientCommand.getDescription())
                .id(ingredientCommand.getId())
                .uom(uomConverter.convert(ingredientCommand.getUnitOfMeasure()))
                .build();
    }
}
