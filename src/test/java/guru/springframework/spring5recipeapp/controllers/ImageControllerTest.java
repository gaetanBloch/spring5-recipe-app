package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.ImageService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {
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
        mockMvc.perform(get("/recipe/" + ID + "/image"))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/imageuploadform"));

        verify(recipeService).findCommandById(ID);
    }

    @Test
    public void uploadImageTest() throws Exception {
        // Given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "imagefile", "test.txt", "text/plain", "text".getBytes());

        // When
        mockMvc.perform(multipart("/recipe/" + ID + "/image").file(multipartFile))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/recipe/" + ID + "/show"))
                .andExpect(view().name("redirect:/recipe/" + ID + "/show"));

        verify(imageService).saveImageFile(eq(ID), any());
    }
}