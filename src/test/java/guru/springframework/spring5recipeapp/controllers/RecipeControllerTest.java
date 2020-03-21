package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerTest {
    private static final Long ID = 1L;

    RecipeController recipeController;
    @Mock
    RecipeService recipeService;

    @Before
    public void setUp() {
        recipeController = new RecipeController(recipeService);
    }

    @Test
    public void getRecipeTest() throws Exception {
        // Given
        when(recipeService.findById(ID)).thenReturn(Recipe.builder().id(ID).build());
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

        // When
        mockMvc.perform(get("/recipe/show/" + ID))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"));
    }
}