package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("Saving an image for recipe with id = " + recipeId);

        try {
            Optional<Recipe> recipe = recipeRepository.findById(recipeId);

            if (!recipe.isPresent()) {
                log.error("Recipe not found for id = " + recipeId);
                throw new RuntimeException("Recipe not found for id = " + recipeId);
            }

            Byte[] imageBytes = new Byte[file.getBytes().length];
            for (int i = 0; i < file.getBytes().length; i++) {
                imageBytes[i] = file.getBytes()[i];
            }
            recipe.get().setImage(imageBytes);

            recipeRepository.save(recipe.get());

        } catch (IOException e) {
            log.error("IOException occurred", e);
            throw new RuntimeException("IOException occurred", e);
        }
    }
}
