package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.TestUtils;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Transactional
    @Test
    public void saveRecipeDescriptionTest() {
        // Given
        Recipe recipe = recipeRepository.findAll().iterator().next();
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

        // When
        if (recipeCommand != null) {
            recipeCommand.setDescription(TestUtils.DESCRIPTION);
        }
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        // Then
        assertNotNull(savedRecipeCommand);
        assertEquals(TestUtils.DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(recipe.getId(), savedRecipeCommand.getId());
        assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }
}
