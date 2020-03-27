package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5recipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    @Transactional
    public IngredientCommand findByRecipeIdByIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if (!recipe.isPresent()) {
            log.error("Recipe not found for id = " + recipeId);
            throw new NotFoundException("Recipe not found for id = " + recipeId);
        }

        Optional<IngredientCommand> ingredientCommand = recipe.get().getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        if (!ingredientCommand.isPresent()) {
            log.error("Ingredient not found for id = " + ingredientId);
            throw new NotFoundException("Ingredient not found for id = " + ingredientId);
        }

        return ingredientCommand.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipe = recipeRepository.findById(ingredientCommand.getRecipeId());

        if (!recipe.isPresent()) {
            log.error("Recipe not found for id = " + ingredientCommand.getRecipeId());
            throw new NotFoundException("Recipe not found for id = " + ingredientCommand.getRecipeId());
        }

        Optional<Ingredient> ingredientOptional = recipe.get().getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            // Update recipe's ingredient case
            ingredientOptional.get().setDescription(ingredientCommand.getDescription());
            ingredientOptional.get().setAmount(ingredientCommand.getAmount());
            ingredientOptional.get().setUom(unitOfMeasureRepository
                    .findById(ingredientCommand.getUom().getId())
                    .orElseThrow(() -> new NotFoundException("Unit of measure not found for id = " +
                            ingredientCommand.getUom().getId())));
        } else {
            // Create new recipe's ingredient case
            recipe.get().addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
        }

        Recipe savedRecipe = recipeRepository.save(recipe.get());

        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        // Check by description if it didn't match the id
        if (!savedIngredient.isPresent()) {
            savedIngredient = savedRecipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getDescription().equals(ingredientCommand.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(ingredientCommand.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(ingredientCommand.getUom().getId()))
                    .findFirst();
        }

        return ingredientToIngredientCommand.convert(savedIngredient.orElseThrow(() ->
                new NotFoundException("Ingredient not found for id = " + ingredientCommand.getId())));
    }

    @Override
    public void deleteByRecipeIdByIngredientId(Long recipeId, Long ingredientId) {
        log.debug("Deleting ingredient [" + ingredientId + "] of recipe [" + recipeId + "]");

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if (recipe.isPresent()) {
            log.debug("Found recipe [" + recipeId + "]");

            Optional<Ingredient> ingredientOptional = recipe.get().getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                log.debug("Found ingredient [" + ingredientId + "]");

                ingredientOptional.get().setRecipe(null);
                recipe.get().getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe.get());
            } else {
                log.error("Ingredient not found for id = " + ingredientId);
                throw new NotFoundException("Ingredient not found for id = " + ingredientId);
            }
        } else {
            log.error("Recipe not found for id = " + recipeId);
            throw new NotFoundException("Recipe not found for id = " + recipeId);
        }
    }
}
