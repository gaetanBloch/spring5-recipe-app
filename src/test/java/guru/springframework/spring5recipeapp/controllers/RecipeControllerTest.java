package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerTest {
    private MockMvc mockMvc;
    @Mock
    private RecipeService recipeService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RecipeController(recipeService)).build();
    }

    @Test
    public void getRecipeTest() throws Exception {
        // Given
        when(recipeService.findById(ID)).thenReturn(Recipe.builder().id(ID).build());

        // When
        mockMvc.perform(get("/recipe/" + ID +"/show"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void getNewRecipeFormTest() throws Exception {
        // When
        mockMvc.perform(get("/recipe/new"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void postNewRecipeFormTest() throws Exception {
        // Given
        when(recipeService.saveRecipeCommand(any())).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("description", "description")
        )

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + ID +"/show" ));
    }

    @Test
    public void getUpdateRecipeFormTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get("/recipe/" + ID + "/update"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }
}