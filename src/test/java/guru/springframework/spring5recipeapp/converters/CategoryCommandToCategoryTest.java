package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.DESCRIPTION;
import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class CategoryCommandToCategoryTest {
    private CategoryCommandToCategory converter;

    @Before
    public void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    public void convertTest() {
        // Given
        CategoryCommand categoryCommand = CategoryCommand.builder()
                .description(DESCRIPTION)
                .id(ID)
                .build();
        Category category;

        // When
        category = converter.convert(categoryCommand);

        // Then
        assertNotNull(category);
        assertEquals(ID, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }
}