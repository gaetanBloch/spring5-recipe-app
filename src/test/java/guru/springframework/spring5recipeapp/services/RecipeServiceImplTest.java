package guru.springframework.spring5recipeapp.services;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.TestUtils;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
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

    private RecipeServiceImpl recipeService;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RecipeCommandToRecipe recipeCommandToRecipe;
    @Mock
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Before
    public void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
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
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(TestUtils.ID).build());
        when(recipeRepository.findById(TestUtils.ID)).thenReturn(recipe);
        Recipe recipeReturned;

        // When
        recipeReturned = recipeService.findById(TestUtils.ID);

        //Then
        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeRepository, times(1)).findById(TestUtils.ID);
        verify(recipeRepository, never()).findAll();
    }

    @Test(expected = RuntimeException.class)
    public void getRecipeByIdNotFoundTest() {
        // Given
        when(recipeRepository.findById(TestUtils.ID)).thenReturn(Optional.empty());

        // When
        recipeService.findById(TestUtils.ID);

        // Then
        // RuntimeException thrown
    }

    @Test
    public void saveRecipeCommandTest() {
        // Given
        Recipe recipe = Recipe.builder().id(TestUtils.ID).description(TestUtils.DESCRIPTION).build();
        RecipeCommand recipeCommand = RecipeCommand.builder().id(TestUtils.ID).description(TestUtils.DESCRIPTION).build();
        when(recipeCommandToRecipe.convert(recipeCommand)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);
        RecipeCommand savedRecipeCommand;

        // When
        savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        // Then
        assertNotNull(savedRecipeCommand);
        assertEquals(TestUtils.ID, savedRecipeCommand.getId());
        assertEquals(TestUtils.DESCRIPTION, savedRecipeCommand.getDescription());
        verify(recipeCommandToRecipe).convert(recipeCommand);
        verify(recipeRepository).save(recipe);
        verify(recipeToRecipeCommand).convert(recipe);
    }

    @Test(expected = NullPointerException.class)
    public void saveRecipeCommandNullTest() {
        // When
        recipeService.saveRecipeCommand(null);

        // Then
        // NPE thrown
    }
}