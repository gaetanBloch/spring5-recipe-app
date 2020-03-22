package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public interface IngredientService {
    IngredientCommand findByRecipeIdByIngredientId(Long recipeId, Long ingredientId);
}
