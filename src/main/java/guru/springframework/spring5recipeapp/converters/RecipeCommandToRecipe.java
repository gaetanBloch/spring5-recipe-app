package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Component
@RequiredArgsConstructor
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    @Nullable
    @Synchronized
    @Override
    public Recipe convert(@Nullable RecipeCommand recipeCommand) {

        if (recipeCommand == null) {
            return null;
        }

        return Recipe.builder()
                .id(recipeCommand.getId())
                .cookTime(recipeCommand.getCookTime())
                .description(recipeCommand.getDescription())
                .difficulty(recipeCommand.getDifficulty())
                .directions(recipeCommand.getDirections())
                .prepTime(recipeCommand.getPrepTime())
                .servings(recipeCommand.getServings())
                .source(recipeCommand.getSource())
                .url(recipeCommand.getUrl())
                .notes(notesConverter.convert(recipeCommand.getNotes()))
                .ingredients(recipeCommand.getIngredients() == null ? new HashSet<>() : recipeCommand
                        .getIngredients()
                        .stream()
                        .map(ingredientConverter::convert)
                        .collect(Collectors.toSet()))
                .categories(recipeCommand.getCategories() == null ? new HashSet<>() : recipeCommand
                        .getCategories()
                        .stream()
                        .map(categoryConverter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }
}
