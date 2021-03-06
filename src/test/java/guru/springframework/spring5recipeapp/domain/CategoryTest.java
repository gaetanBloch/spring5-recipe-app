package guru.springframework.spring5recipeapp.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {
    private Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void getIdTest() {
        // Given
        final Long idValue = 4L;

        // When
        category.setId(idValue);

        // Then
        assertEquals(idValue, category.getId());
    }
}