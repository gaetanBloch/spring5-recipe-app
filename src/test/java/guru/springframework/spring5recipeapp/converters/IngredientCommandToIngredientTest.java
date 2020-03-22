package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class IngredientCommandToIngredientTest {
    private IngredientCommandToIngredient converter;

    @Before
    public void setUp() {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    public void convertTest() {
        // Given
        Ingredient ingredient;
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .uom(UnitOfMeasureCommand.builder().id(UOM_ID).build())
                .id(ID)
                .description(DESCRIPTION)
                .amount(AMOUNT)
                .build();

        // When
        ingredient = converter.convert(ingredientCommand);

        // Then
        assertNotNull(ingredient);
        assertNotNull(ingredient.getUom());
        assertEquals(ID, ingredient.getId());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(UOM_ID, ingredient.getUom().getId());
    }

    @Test
    public void convertWithNullUOMTest() {
        // Given
        Ingredient ingredient;
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .id(ID)
                .description(DESCRIPTION)
                .amount(AMOUNT)
                .build();

        // When
        ingredient = converter.convert(ingredientCommand);

        // Then
        assertNotNull(ingredient);
        assertNull(ingredient.getUom());
        assertEquals(ID, ingredient.getId());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(AMOUNT, ingredient.getAmount());
    }
}