package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class IngredientToIngredientCommandTest {
    private IngredientToIngredientCommand converter;

    @Before
    public void setUp() {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    public void convertTest() {
        // Given
        IngredientCommand ingredientCommand;
        Ingredient ingredient = Ingredient.builder()
                .uom(UnitOfMeasure.builder().id(UOM_ID).build())
                .id(ID)
                .description(DESCRIPTION)
                .amount(AMOUNT)
                .build();

        // When
        ingredientCommand = converter.convert(ingredient);

        // Then
        assertNotNull(ingredientCommand);
        assertNotNull(ingredientCommand.getUnitOfMeasure());
        assertEquals(ID, ingredientCommand.getId());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(UOM_ID, ingredientCommand.getUnitOfMeasure().getId());
    }

    @Test
    public void convertWithNullUOMTest() {
        // Given
        IngredientCommand ingredientCommand;
        Ingredient ingredient = Ingredient.builder()
                .id(ID)
                .description(DESCRIPTION)
                .amount(AMOUNT)
                .build();

        // When
        ingredientCommand = converter.convert(ingredient);

        // Then
        assertNotNull(ingredientCommand);
        assertNull(ingredientCommand.getUnitOfMeasure());
        assertEquals(ID, ingredientCommand.getId());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
    }
}