package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.DESCRIPTION;
import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class UnitOfMeasureToUnitOfMeasureCommandTest {
    private UnitOfMeasureToUnitOfMeasureCommand converter;

    @Before
    public void setUp() {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    public void convertTest() {
        // Given
        UnitOfMeasure uom = UnitOfMeasure.builder()
                .description(DESCRIPTION)
                .id(ID)
                .build();

        // When
        UnitOfMeasureCommand uomCommand = converter.convert(uom);

        // Then
        assertNotNull(uomCommand);
        assertEquals(ID, uomCommand.getId());
        assertEquals(DESCRIPTION, uomCommand.getDescription());
    }
}