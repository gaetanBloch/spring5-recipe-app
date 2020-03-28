package guru.springframework.spring5recipeapp.controllers;

import com.google.common.collect.ImmutableMap;
import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
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
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.TestUtils.ID2;
import static guru.springframework.spring5recipeapp.controllers.ControllerExceptionHandler.ATTRIBUTE_EXCEPTION;
import static guru.springframework.spring5recipeapp.controllers.ControllerExceptionHandler.VIEW_404_NOT_FOUND;
import static guru.springframework.spring5recipeapp.controllers.IngredientController.*;
import static guru.springframework.spring5recipeapp.controllers.RecipeController.ATTRIBUTE_RECIPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class IngredientControllerTest {
    private static final Map<String, String> URI_VARIABLES = ImmutableMap.of(
            "recipeId", ID.toString(),
            "id", ID2.toString()
    );
    private static final URI URI_INGREDIENT = new UriTemplate(URL_INGREDIENT).expand(URI_VARIABLES);
    private static final URI URI_INGREDIENTS = new UriTemplate(URL_INGREDIENTS).expand(URI_VARIABLES);
    private static final URI URI_INGREDIENT_SHOW = new UriTemplate(URL_INGREDIENT_SHOW).expand(URI_VARIABLES);
    private static final URI URI_INGREDIENT_NEW = new UriTemplate(URL_INGREDIENT_NEW).expand(URI_VARIABLES);
    private static final URI URI_INGREDIENT_UPDATE = new UriTemplate(URL_INGREDIENT_UPDATE).expand(URI_VARIABLES);
    private static final URI URI_INGREDIENT_DELETE = new UriTemplate(URL_INGREDIENT_DELETE).expand(URI_VARIABLES);

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void listIngredientsTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get(URI_INGREDIENTS))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INGREDIENT_LIST))
                .andExpect(model().attributeExists(ATTRIBUTE_RECIPE));

        verify(recipeService).findCommandById(ID);
    }

    @Test
    public void listIngredientsRecipeNotFoundTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenThrow(NotFoundException.class);

        // When
        mockMvc.perform(get(URI_INGREDIENTS))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void showIngredientTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenReturn(new IngredientCommand());

        // When
        mockMvc.perform(get(URI_INGREDIENT_SHOW))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_INGREDIENT_SHOW))
                .andExpect(model().attributeExists(ATTRIBUTE_INGREDIENT));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID2);
    }

    @Test
    public void showIngredientNotFoundTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenThrow(NotFoundException.class);

        // When
        mockMvc.perform(get(URI_INGREDIENT_SHOW))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void newIngredientFormButRecipeNotFoundTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenThrow(NotFoundException.class);

        // When
        mockMvc.perform(get(URI_INGREDIENT_NEW))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void updateIngredientFormTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenReturn(new IngredientCommand());
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        // When
        mockMvc.perform(get(URI_INGREDIENT_UPDATE))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM))
                .andExpect(model().attributeExists(ATTRIBUTE_INGREDIENT))
                .andExpect(model().attributeExists(ATTRIBUTE_UOM_LIST));

        verify(ingredientService).findByRecipeIdByIngredientId(ID, ID2);
        verify(unitOfMeasureService).listAllUoms();
    }

    @Test
    public void updateIngredientFormButRecipeNotFoundTest() throws Exception {
        // Given
        when(ingredientService.findByRecipeIdByIngredientId(ID, ID2)).thenThrow(NotFoundException.class);

        // When
        mockMvc.perform(get(URI_INGREDIENT_UPDATE))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void saveOrUpdateTest() throws Exception {
        // Given
        when(ingredientService.saveIngredientCommand(any()))
                .thenReturn(IngredientCommand.builder().id(ID2).recipeId(ID).build());

        // When
        mockMvc.perform(post(URI_INGREDIENT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("description", "description")
        )

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + URI_INGREDIENT_SHOW));

        verify(ingredientService).saveIngredientCommand(any());
    }

    @Test
    public void saveOrUpdateRecipeNotFoundTest() throws Exception {
        // Given
        when(ingredientService.saveIngredientCommand(any())).thenThrow(NotFoundException.class);

        // When
        mockMvc.perform(post(URI_INGREDIENT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("description", "description")
        )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }

    @Test
    public void deleteIngredientTest() throws Exception {
        // When
        mockMvc.perform(get(URI_INGREDIENT_DELETE))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + URI_INGREDIENTS));

        verify(ingredientService).deleteByRecipeIdByIngredientId(ID, ID2);
    }

    @Test
    public void deleteIngredientNotFoundTest() throws Exception {
        // Given
        doThrow(NotFoundException.class).when(ingredientService).deleteByRecipeIdByIngredientId(ID, ID2);

        // When
        mockMvc.perform(get(URI_INGREDIENT_DELETE))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND))
                .andExpect(model().attributeExists(ATTRIBUTE_EXCEPTION));
    }
}