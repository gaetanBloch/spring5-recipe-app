package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5recipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
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
    private RecipeRepository recipeRepository;
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() {
        ingredientService = new IngredientServiceImpl(
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
                recipeRepository,
                unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdByIngredientIdTest() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        recipe.addIngredient(Ingredient.builder().id(ID).build());
        recipe.addIngredient(Ingredient.builder().id(ID2).build());
        recipe.addIngredient(Ingredient.builder().id(ID3).build());
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(ID)).thenReturn(recipeOptional);

        // When
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdByIngredientId(ID, ID3);

        // Then
        assertNotNull(ingredientCommand);
        assertEquals(ID3, ingredientCommand.getId());
        assertEquals(ID, ingredientCommand.getRecipeId());
        verify(recipeRepository).findById(ID);
    }

    @Test(expected = RuntimeException.class)
    public void findByRecipeIdByIngredientIdButRecipeNotFoundTest() {
        // Given
        when(recipeRepository.findById(ID)).thenReturn(Optional.empty());

        // When
        ingredientService.findByRecipeIdByIngredientId(ID, ID3);

        // Then
        // RuntimeException thrown
    }

    @Test(expected = RuntimeException.class)
    public void findByRecipeIdByIngredientIdButIngredientNotFoundTest() {
        // Given
        Optional<Recipe> recipe = Optional.of(Recipe.builder().id(ID).build());
        when(recipeRepository.findById(ID)).thenReturn(recipe);

        // When
        ingredientService.findByRecipeIdByIngredientId(ID, ID3);

        // Then
        // RuntimeException thrown
    }

    @Test
    public void saveRecipeIngredientTest() {
        // Given
        // We rely on the ingredient id here
        IngredientCommand ingredientCommand = IngredientCommand.builder().id(ID3).recipeId(ID2).build();
        Recipe recipe = new Recipe();
        recipe.setId(ID2);
        recipe.addIngredient(Ingredient.builder().id(ID3).build());
        when(recipeRepository.findById(ID2)).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any())).thenReturn(recipe);

        // When
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        // Then
        assertNotNull(savedIngredientCommand);
        assertEquals(ID3, savedIngredientCommand.getId());
        assertEquals(ID2, savedIngredientCommand.getRecipeId());
        verify(recipeRepository).findById(ID2);
        verify(recipeRepository).save(any());
    }

    @Test
    public void createRecipeIngredientTest() {
        // Given
        // For new ingredient the id is not set so we rely on the description, amount and UOM
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .recipeId(ID2)
                .description(DESCRIPTION)
                .amount(AMOUNT)
                .uom(UnitOfMeasureCommand.builder().id(UOM_ID).build())
                .build();
        Recipe recipe = new Recipe();
        recipe.setId(ID2);
        recipe.addIngredient(Ingredient.builder()
                .id(ID3)
                .description(DESCRIPTION)
                .amount(AMOUNT)
                .uom(UnitOfMeasure.builder().id(UOM_ID).build())
                .build());
        when(recipeRepository.findById(ID2)).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any())).thenReturn(recipe);

        // When
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        // Then
        assertNotNull(savedIngredientCommand);
        assertEquals(ID3, savedIngredientCommand.getId());
        assertEquals(ID2, savedIngredientCommand.getRecipeId());
        verify(recipeRepository).findById(ID2);
        verify(recipeRepository).save(any());
    }

    @Test(expected = RuntimeException.class)
    public void createRecipeIngredientButRecipeNotFoundTest() {
        // Given
        IngredientCommand ingredientCommand = IngredientCommand.builder().recipeId(ID2).build();
        when(recipeRepository.findById(ID2)).thenReturn(Optional.empty());

        // When
        ingredientService.saveIngredientCommand(ingredientCommand);

        // Then
        // RuntimeException thrown
    }

    @Test(expected = RuntimeException.class)
    public void createRecipeIngredientButIngredientNotFound() {
        // Given
        IngredientCommand ingredientCommand = IngredientCommand.builder().id(ID3).recipeId(ID2).build();
        when(recipeRepository.findById(ID2)).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any())).thenReturn(new Recipe());

        // When
        ingredientService.saveIngredientCommand(ingredientCommand);

        // Then
        // RuntimeException thrown
    }

    @Test
    public void updateRecipeIngredientTest() {
        // Given
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .id(ID3)
                .recipeId(ID2)
                .uom(UnitOfMeasureCommand.builder().id(ID).build())
                .build();
        Recipe recipe = new Recipe();
        recipe.setId(ID2);
        recipe.addIngredient(Ingredient.builder().id(ID3).build());
        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        recipeOptional.get().addIngredient(Ingredient.builder().id(ID3).build());
        when(recipeRepository.findById(ID2)).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(recipe);
        when(unitOfMeasureRepository.findById(ID)).thenReturn(Optional.of(new UnitOfMeasure()));

        // When
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        // Then
        assertNotNull(savedIngredientCommand);
        assertEquals(ID3, savedIngredientCommand.getId());
        assertEquals(ID2, savedIngredientCommand.getRecipeId());
        verify(recipeRepository).findById(ID2);
        verify(recipeRepository).save(any());
        verify(unitOfMeasureRepository).findById(ID);
    }

    @Test(expected = RuntimeException.class)
    public void updateRecipeIngredientButUOMNotFoundTest() {
        // Given
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .id(ID3)
                .recipeId(ID2)
                .uom(UnitOfMeasureCommand.builder().id(ID).build())
                .build();
        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        recipeOptional.get().addIngredient(Ingredient.builder().id(ID3).build());
        when(recipeRepository.findById(ID2)).thenReturn(recipeOptional);
        when(unitOfMeasureRepository.findById(ID)).thenReturn(Optional.empty());

        // When
        ingredientService.saveIngredientCommand(ingredientCommand);

        // Then
        // RuntimeException thrown
    }
}