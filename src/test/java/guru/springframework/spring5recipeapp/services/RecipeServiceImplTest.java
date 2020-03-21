package guru.springframework.spring5recipeapp.services;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {
    private static final Long ID = 1L;

    RecipeServiceImpl recipeService;
    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    public void getRecipesTest() {
        // Given
        Set<Recipe> recipes;
        when(recipeRepository.findAll()).thenReturn(ImmutableSet.of(new Recipe()));

        // When
        recipes = recipeService.getRecipes();

        // Then
        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    public void getRecipeByIdTest() {
        // Given
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(ID).build());
        when(recipeRepository.findById(ID)).thenReturn(recipe);
        Recipe recipeReturned;

        // When
        recipeReturned = recipeService.findById(ID);

        //Then
        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeRepository, times(1)).findById(ID);
        verify(recipeRepository, never()).findAll();
    }

    @Test(expected = RuntimeException.class)
    public void getRecipeByIdNotFoundTest() {
        // Given
        when(recipeRepository.findById(ID)).thenReturn(Optional.empty());

        // When
        recipeService.findById(ID);

        // Then
        // RuntimeException thrown
    }
}