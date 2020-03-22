package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
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
import static org.mockito.Mockito.*;
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
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        IngredientController controller = new IngredientController(recipeService);
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

        verify(recipeService, times(1)).findCommandById(ID);
    }
}