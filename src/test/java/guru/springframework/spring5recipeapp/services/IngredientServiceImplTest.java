package guru.springframework.spring5recipeapp.services;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static guru.springframework.spring5recipeapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceImplTest {
    private IngredientService ingredientService;
    @Mock
    private IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, recipeRepository);
    }

    @Test
    public void findByRecipeIdByIngredientIdTest() {
        // Given
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(ID).ingredients(ImmutableSet.of(
                Ingredient.builder().id(ID).build(),
                Ingredient.builder().id(ID2).build(),
                Ingredient.builder().id(ID3).build()
        )).build());
        when(recipeRepository.findById(ID)).thenReturn(recipe);
        when(ingredientToIngredientCommand.convert(any()))
                .thenReturn(IngredientCommand.builder().id(ID3).recipeId(ID).build());

        // When
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdByIngredientId(ID, ID3);

        // Then
        assertNotNull(ingredientCommand);
        assertEquals(ID3, ingredientCommand.getId());
        assertEquals(ID, ingredientCommand.getRecipeId());
        verify(recipeRepository).findById(ID);
        verify(ingredientToIngredientCommand).convert(any());
    }

    @Test(expected = RuntimeException.class)
    public void findByRecipeIdByIngredientIdRecipeNotFoundTest() {
        // Given
        when(recipeRepository.findById(ID)).thenReturn(Optional.empty());

        // When
        ingredientService.findByRecipeIdByIngredientId(ID, ID3);

        // Then
        // RuntimeException thrown
    }

    @Test(expected = RuntimeException.class)
    public void findByRecipeIdByIngredientIdIngredientNotFoundTest() {
        // Given
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(ID).build());
        when(recipeRepository.findById(ID)).thenReturn(recipe);

        // When
        ingredientService.findByRecipeIdByIngredientId(ID, ID3);

        // Then
        // RuntimeException thrown
    }
}