package guru.springframework.spring5recipeapp.controllers;

import com.google.common.collect.ImmutableMap;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.services.CategoryService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Map;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.controllers.RecipeController.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerTest {
    private static final Map<String, String> URI_VARIABLES = ImmutableMap.of("id", ID.toString());
    private static final URI URI_RECIPE_SHOW = new UriTemplate(URL_RECIPE_SHOW).expand(URI_VARIABLES);
    private static final URI URI_RECIPE_UPDATE = new UriTemplate(URL_RECIPE_UPDATE).expand(URI_VARIABLES);
    private static final URI URI_RECIPE_DELETE = new UriTemplate(URL_RECIPE_DELETE).expand(URI_VARIABLES);

    private MockMvc mockMvc;
    @Mock
    private RecipeService recipeService;
    @Mock
    private CategoryService categoryService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RecipeController(recipeService, categoryService)).build();
    }

    @Test
    public void getRecipeTest() throws Exception {
        // Given
        when(recipeService.findById(ID)).thenReturn(Recipe.builder().id(ID).build());

        // When
        mockMvc.perform(get(URI_RECIPE_SHOW))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_RECIPE_SHOW_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_RECIPE));
    }

    @Test
    public void getRecipeNotFoundTest() throws Exception {
        // Given
        when(recipeService.findById(ID)).thenThrow(NotFoundException.class);

        // When
        mockMvc.perform(get(URI_RECIPE_SHOW))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void getRecipeNumberFormatExceptionTest() throws Exception {
        // When
        mockMvc.perform(get(URL_RECIPE + "/foo/show"))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(view().name(VIEW_400_BAD_REQUEST))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void getNewRecipeFormTest() throws Exception {
        // When
        mockMvc.perform(get(URL_RECIPE_NEW))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_RECIPE_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_RECIPE));
    }

    @Test
    public void postNewRecipeFormTest() throws Exception {
        // Given
        when(recipeService.saveRecipeCommand(any())).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(post(URL_RECIPE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("description", "description")
        )

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + URI_RECIPE_SHOW));
    }

    @Test
    public void getUpdateRecipeFormTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get(URI_RECIPE_UPDATE))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_RECIPE_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_RECIPE));
    }

    @Test
    public void deleteRecipeTest() throws Exception {
        // When
        mockMvc.perform(get(URI_RECIPE_DELETE))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(ID);
    }
}