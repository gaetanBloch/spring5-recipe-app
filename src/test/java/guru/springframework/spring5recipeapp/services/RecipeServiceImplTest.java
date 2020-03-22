package guru.springframework.spring5recipeapp.services;

import com.google.common.collect.ImmutableSet;
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

import static guru.springframework.spring5recipeapp.TestUtils.DESCRIPTION;
import static guru.springframework.spring5recipeapp.TestUtils.ID;
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
        when(recipeRepository.findAll()).thenReturn(ImmutableSet.of(new Recipe()));

        // When
        Set<Recipe> recipes = recipeService.getRecipes();

        // Then
        assertEquals(1, recipes.size());
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    public void getRecipeByIdTest() {
        // Given
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(ID).build());
        when(recipeRepository.findById(ID)).thenReturn(recipe);

        // When
        Recipe recipeReturned = recipeService.findById(ID);

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

    @Test
    public void saveRecipeCommandTest() {
        // Given
        Recipe recipe = Recipe.builder().id(ID).description(DESCRIPTION).build();
        RecipeCommand recipeCommand = RecipeCommand.builder().id(ID).description(DESCRIPTION).build();
        when(recipeCommandToRecipe.convert(recipeCommand)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);

        // When
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        // Then
        assertNotNull(savedRecipeCommand);
        assertEquals(ID, savedRecipeCommand.getId());
        assertEquals(DESCRIPTION, savedRecipeCommand.getDescription());
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

    @Test
    public void findCommandByIdTest() {
        // Given
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(ID).build());
        when(recipeRepository.findById(ID)).thenReturn(recipe);
        when(recipeToRecipeCommand.convert(recipe.get())).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        RecipeCommand commandById = recipeService.findCommandById(ID);

        // Then
        assertNotNull("Null recipe returned", commandById);
        verify(recipeRepository, times(1)).findById(ID);
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void deleteByIdTest() {
        // When
        recipeService.deleteById(ID);

        // Then
        verify(recipeRepository, times(1)).deleteById(ID);
    }
}