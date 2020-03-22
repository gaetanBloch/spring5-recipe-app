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
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {
    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(@Nullable Ingredient ingredient) {

        if (ingredient == null) {
            return null;
        }

        return IngredientCommand.builder()
                .amount(ingredient.getAmount())
                .description(ingredient.getDescription())
                .id(ingredient.getId())
                .uom(uomConverter.convert(ingredient.getUom()))
                .build();
    }
}
