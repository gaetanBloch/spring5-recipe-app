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
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenReturn(new IngredientCommand());

        // When
        mockMvc.perform(get("/recipe/" + ID + "/ingredient/" + ID2 + "/show"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID2);
    }

    @Test
    public void newIngredientFormTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).build());
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        // When
        mockMvc.perform(get("/recipe/" + ID + "/ingredient/new"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(recipeService).findCommandById(ID);
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    public void updateIngredientFormTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenReturn(new IngredientCommand());
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        // When
        mockMvc.perform(get("/recipe/" + ID + "/ingredient/" + ID2 + "/update"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID2);
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    public void saveOrUpdateTest() throws Exception {
        // Given
        when(ingredientService.saveIngredientCommand(any()))
                .thenReturn(IngredientCommand.builder().id(ID2).recipeId(ID).build());

        // When
        mockMvc.perform(post("/recipe/" + ID + "/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("description", "description")
        )

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + ID + "/ingredient/" + ID2 + "/show"));

        verify(ingredientService).saveIngredientCommand(any());
    }
}