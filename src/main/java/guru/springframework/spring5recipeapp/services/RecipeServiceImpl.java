package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Getting recipes from service...");
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        log.debug("Getting recipes from service... OK");
        return recipes;
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (!recipe.isPresent()) {
            throw new RuntimeException("Recipe Not Found!");
        }
        return recipe.get();
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

        if (detachedRecipe == null) {
            throw new NullPointerException("Recipe should not be null!");
        }

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe id = " + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }
}
