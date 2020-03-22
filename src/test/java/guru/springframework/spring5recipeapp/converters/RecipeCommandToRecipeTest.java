package guru.springframework.spring5recipeapp.converters;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.NotesCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class RecipeCommandToRecipeTest {
    private RecipeCommandToRecipe converter;

    @Before
    public void setUp() {
        converter = new RecipeCommandToRecipe(
                new CategoryCommandToCategory(),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
                new NotesCommandToNotes()
        );
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    public void convertTest() {
        // Given
        RecipeCommand recipeCommand = RecipeCommand.builder()
                .id(ID)
                .description(DESCRIPTION)
                .cookTime(COOK_TIME)
                .difficulty(DIFFICULTY)
                .directions(DIRECTIONS)
                .prepTime(PREP_TIME)
                .servings(SERVINGS)
                .source(SOURCE)
                .url(URL)
                .notes(NotesCommand.builder().id(NOTES_ID).recipeNotes(RECIPE_NOTES).build())
                .categories(ImmutableSet.of(
                        CategoryCommand.builder().id(CAT_ID_1).build(),
                        CategoryCommand.builder().id(CAT_ID_2).build())
                )
                .ingredients(ImmutableSet.of(
                        IngredientCommand.builder().id(INGREDIENT_ID_1).build(),
                        IngredientCommand.builder().id(INGREDIENT_ID_2).build()
                ))
                .build();

        // When
        Recipe recipe = converter.convert(recipeCommand);

        // Then
        assertNotNull(recipe);
        assertNotNull(recipe.getNotes());
        assertNotNull(recipe.getCategories());
        assertNotNull(recipe.getIngredients());
        assertEquals(ID, recipe.getId());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(2, recipe.getCategories().size());
        assertEquals(2, recipe.getIngredients().size());
    }
}