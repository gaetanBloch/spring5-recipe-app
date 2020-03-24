package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Component
@RequiredArgsConstructor
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {
    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(@Nullable Recipe recipe) {

        if (recipe == null) {
            return null;
        }

        return RecipeCommand.builder()
                .id(recipe.getId())
                .cookTime(recipe.getCookTime())
                .description(recipe.getDescription())
                .difficulty(recipe.getDifficulty())
                .directions(recipe.getDirections())
                .prepTime(recipe.getPrepTime())
                .servings(recipe.getServings())
                .source(recipe.getSource())
                .url(recipe.getUrl())
                .image(recipe.getImage())
                .notes(notesConverter.convert(recipe.getNotes()))
                .ingredients(recipe
                        .getIngredients()
                        .stream()
                        .map(ingredientConverter::convert)
                        .collect(Collectors.toSet()))
                .categories(recipe
                        .getCategories()
                        .stream()
                        .map(categoryConverter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }
}
