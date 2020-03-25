package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.IngredientService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import guru.springframework.spring5recipeapp.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.TestUtils.ID2;
import static guru.springframework.spring5recipeapp.controllers.IngredientController.*;
import static guru.springframework.spring5recipeapp.controllers.RecipeController.ATTRIBUTE_RECIPE;
import static guru.springframework.spring5recipeapp.controllers.RecipeController.URL_RECIPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class IngredientControllerTest {
    private static final String URL_INGREDIENT_SHOW = URL_RECIPE + "/" + ID + "/ingredient/" + ID2 + "/show";
    private static final String URL_INGREDIENTS = URL_RECIPE + "/" + ID + "/ingredients";

    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private UnitOfMeasureService unitOfMeasureService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        IngredientController controller = new IngredientController(
                recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void listIngredientsTest() throws Exception {
        // Given
        when(recipeService.findCommandById(anyLong())).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get(URL_INGREDIENTS))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INGREDIENT_LIST))
                .andExpect(model().attributeExists(ATTRIBUTE_RECIPE));

        verify(recipeService).findCommandById(ID);
    }

    @Test
    public void showIngredientTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenReturn(new IngredientCommand());

        // When
        mockMvc.perform(get(URL_INGREDIENT_SHOW))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INGREDIENT_SHOW))
                .andExpect(model().attributeExists(ATTRIBUTE_INGREDIENT));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID2);
    }

    @Test
    public void newIngredientFormTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).build());
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        // When
        mockMvc.perform(get(URL_RECIPE + "/" + ID + "/ingredient/new"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_INGREDIENT))
                .andExpect(model().attributeExists(ATTRIBUTE_UOM_LIST));

        verify(recipeService).findCommandById(ID);
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    public void updateIngredientFormTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenReturn(new IngredientCommand());
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        // When
        mockMvc.perform(get(URL_RECIPE + "/" + ID + "/ingredient/" + ID2 + "/update"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_INGREDIENT))
                .andExpect(model().attributeExists(ATTRIBUTE_UOM_LIST));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID2);
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    public void saveOrUpdateTest() throws Exception {
        // Given
        when(ingredientService.saveIngredientCommand(any()))
                .thenReturn(IngredientCommand.builder().id(ID2).recipeId(ID).build());

        // When
        mockMvc.perform(post(URL_RECIPE + "/" + ID + "/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("description", "description")
        )

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + URL_INGREDIENT_SHOW));

        verify(ingredientService).saveIngredientCommand(any());
    }

    @Test
    public void deleteIngredientTest() throws Exception {
        // When
        mockMvc.perform(get(URL_RECIPE + "/" + ID + "/ingredient/" + ID2 + "/delete"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + URL_INGREDIENTS));

        verify(ingredientService).deleteByRecipeIdByIngredientId(ID, ID2);
    }
}