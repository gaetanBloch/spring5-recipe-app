package guru.springframework.spring5recipeapp.services;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.converters.CategoryToCategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.TestUtils.ID2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Gaetan Bloch
 * Created on 27/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, new CategoryToCategoryCommand());
    }

    @Test
    public void findAllTest() {
        // Given
        when(categoryRepository.findAll()).thenReturn(ImmutableSet.of(
                Category.builder().id(ID).build(),
                Category.builder().id(ID2).build()
        ));

        // When
        Set<CategoryCommand> categories = categoryService.findAll();

        // Then
        assertEquals(2, categories.size());
        verify(categoryRepository).findAll();
    }

    @Test
    public void findByIdTest() {
        // Given
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(Category.builder().id(ID).build()));

        // When
        CategoryCommand category = categoryService.findById(ID);

        // Then
        assertNotNull(category);
        assertEquals(ID, category.getId());
        verify(categoryRepository).findById(ID);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundTest() {
        // Given
        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        // When
        categoryService.findById(ID);

        // Then
        // NotFoundException thrown
    }
}