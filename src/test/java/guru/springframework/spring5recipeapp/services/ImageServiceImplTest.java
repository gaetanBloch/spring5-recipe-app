package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {
    private ImageService imageService;
    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void saveImageFileTest() throws IOException {
        // Given
        MultipartFile multipartFile = new MockMultipartFile(
                "imagefile", "test.txt", "text/plain", "text".getBytes());
        when(recipeRepository.findById(ID)).thenReturn(Optional.of(Recipe.builder().id(ID).build()));
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        // When
        imageService.saveImageFile(ID, multipartFile);

        // Then
        verify(recipeRepository).findById(ID);
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

    @Test(expected = RuntimeException.class)
    public void saveImageFileRecipeNotFoundTest() {
        // Given
        MultipartFile multipartFile = new MockMultipartFile(
                "imagefile", "test.txt", "text/plain", "text".getBytes());
        when(recipeRepository.findById(ID)).thenReturn(Optional.empty());

        // When
        imageService.saveImageFile(ID, multipartFile);

        // Then
        // Throws RuntimeException
    }
}