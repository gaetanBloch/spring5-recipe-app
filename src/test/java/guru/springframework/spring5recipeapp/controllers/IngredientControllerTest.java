package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.IngredientService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class IngredientControllerTest {
    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        IngredientController controller = new IngredientController(recipeService, ingredientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void listIngredientsTest() throws Exception {
        // Given
        when(recipeService.findCommandById(anyLong())).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get("/recipe/" + ID + "/ingredients"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(ID);
    }

    @Test
    public void showIngredientTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID))
                .thenReturn(IngredientCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get("/recipe/" + ID + "/ingredient/" + ID + "/show"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID);
    }
}