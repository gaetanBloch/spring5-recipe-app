package guru.springframework.spring5recipeapp.controllers;

import com.google.common.collect.ImmutableMap;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.services.ImageService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Map;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.controllers.AbstractController.VIEW_404_NOT_FOUND;
import static guru.springframework.spring5recipeapp.controllers.ImageController.URL_IMAGE;
import static guru.springframework.spring5recipeapp.controllers.ImageController.URL_RECIPE_IMAGE;
import static guru.springframework.spring5recipeapp.controllers.RecipeController.ATTRIBUTE_RECIPE;
import static guru.springframework.spring5recipeapp.controllers.RecipeController.URL_RECIPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {
    private static final Map<String, String> URI_VARIABLES = ImmutableMap.of("id", ID.toString());
    private static final URI URI_IMAGE = new UriTemplate(URL_IMAGE).expand(URI_VARIABLES);
    private static final URI URI_RECIPE_IMAGE = new UriTemplate(URL_RECIPE_IMAGE).expand(URI_VARIABLES);

    @Mock
    private RecipeService recipeService;
    @Mock
    private ImageService imageService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        ImageController imageController = new ImageController(recipeService, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void showImageUploadFormTest() throws Exception {
        // Given
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).build());

        // When
        mockMvc.perform(get(URI_IMAGE))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ATTRIBUTE_RECIPE))
                .andExpect(view().name(ImageController.VIEW_IMAGE_UPLOAD_FORM));

        verify(recipeService).findCommandById(ID);
    }

    @Test
    public void uploadImageTest() throws Exception {
        // Given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "imagefile", "test.txt", "text/plain", "text".getBytes());

        // When
        mockMvc.perform(multipart(URI_IMAGE).file(multipartFile))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", URL_RECIPE + "/" + ID + "/show"))
                .andExpect(view().name("redirect:" + URL_RECIPE + "/" + ID + "/show"));

        verify(imageService).saveImageFile(eq(ID), any());
    }

    @Test
    public void uploadImageNoRecipeFoundTest() throws Exception {
        // Given
        doThrow(NotFoundException.class).when(imageService).saveImageFile(eq(ID), any());
        MockMultipartFile multipartFile = new MockMultipartFile(
                "imagefile", "test.txt", "text/plain", "text".getBytes());

        // When
        mockMvc.perform(multipart(URI_IMAGE).file(multipartFile))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(view().name(VIEW_404_NOT_FOUND));
    }

    @Test
    public void renderImageFromDBTest() throws Exception {
        // Given
        String string = "Mock text";
        Byte[] bytes = new Byte[string.length()];
        for (int i = 0; i < string.getBytes().length; i++) {
            bytes[i] = string.getBytes()[i];
        }
        when(recipeService.findCommandById(ID)).thenReturn(RecipeCommand.builder().id(ID).image(bytes).build());

        // When
        MockHttpServletResponse response = mockMvc.perform(get(URI_RECIPE_IMAGE))

                // Then
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(string.getBytes().length, response.getContentAsByteArray().length);
        verify(recipeService).findCommandById(ID);
    }
}