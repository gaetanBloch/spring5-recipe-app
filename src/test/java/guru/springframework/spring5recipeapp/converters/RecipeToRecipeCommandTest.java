package guru.springframework.spring5recipeapp.converters;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import guru.springframework.spring5recipeapp.domain.Notes;
import guru.springframework.spring5recipeapp.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class RecipeToRecipeCommandTest {
    private RecipeToRecipeCommand converter;

    @Before
    public void setUp() {
        converter = new RecipeToRecipeCommand(
                new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                new NotesToNotesCommand()
        );
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    public void convertTest() {
        // Given
        Recipe recipe = Recipe.builder()
                .id(ID)
                .description(DESCRIPTION)
                .cookTime(COOK_TIME)
                .difficulty(DIFFICULTY)
                .directions(DIRECTIONS)
                .prepTime(PREP_TIME)
                .servings(SERVINGS)
                .source(SOURCE)
                .url(URL)
                .notes(Notes.builder().id(NOTES_ID).recipeNotes(RECIPE_NOTES).build())
                .categories(ImmutableSet.of(
                        Category.builder().id(CAT_ID_1).build(),
                        Category.builder().id(CAT_ID_2).build())
                )
                .ingredients(ImmutableSet.of(
                        Ingredient.builder().id(INGREDIENT_ID_1).build(),
                        Ingredient.builder().id(INGREDIENT_ID_2).build()
                ))
                .build();

        // When
        RecipeCommand recipeCommand = converter.convert(recipe);

        // Then
        assertNotNull(recipeCommand);
        assertNotNull(recipeCommand.getNotes());
        assertNotNull(recipeCommand.getCategories());
        assertNotNull(recipeCommand.getIngredients());
        assertEquals(ID, recipeCommand.getId());
        assertEquals(DESCRIPTION, recipeCommand.getDescription());
        assertEquals(COOK_TIME, recipeCommand.getCookTime());
        assertEquals(DIFFICULTY, recipeCommand.getDifficulty());
        assertEquals(DIRECTIONS, recipeCommand.getDirections());
        assertEquals(PREP_TIME, recipeCommand.getPrepTime());
        assertEquals(SERVINGS, recipeCommand.getServings());
        assertEquals(SOURCE, recipeCommand.getSource());
        assertEquals(URL, recipeCommand.getUrl());
        assertEquals(NOTES_ID, recipeCommand.getNotes().getId());
        assertEquals(2, recipeCommand.getCategories().size());
        assertEquals(2, recipeCommand.getIngredients().size());
    }
}