package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.services.IngredientService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import guru.springframework.spring5recipeapp.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
@RequiredArgsConstructor
@Slf4j
@Controller
final class IngredientController {
    static final String VIEW_INGREDIENT_LIST = "recipe/ingredient/list";
    static final String VIEW_INGREDIENT_SHOW = "recipe/ingredient/show";
    static final String VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM = "recipe/ingredient/ingredientform";
    static final String ATTRIBUTE_INGREDIENT = "ingredient";
    static final String ATTRIBUTE_UOM_LIST = "uomList";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping(RecipeController.URL_RECIPE + "/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe id = " + recipeId);
        model.addAttribute(RecipeController.ATTRIBUTE_RECIPE, recipeService.findCommandById(Long.valueOf(recipeId)));
        return VIEW_INGREDIENT_LIST;
    }

    @GetMapping(RecipeController.URL_RECIPE + "/{recipeId}/ingredient/{id}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_INGREDIENT, ingredientService.findByRecipeIdByIngredientId(
                Long.valueOf(recipeId), Long.valueOf(id)
        ));
        return VIEW_INGREDIENT_SHOW;
    }

    @GetMapping(RecipeController.URL_RECIPE + "/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        // Throws Exception if the recipe id is not found (Verification that the recipe exists)
        recipeService.findCommandById(Long.valueOf(recipeId));

        model.addAttribute(ATTRIBUTE_INGREDIENT, IngredientCommand.builder()
                // We need to put the recipe id for the POST url form
                .recipeId(Long.valueOf(recipeId))
                // We need to initialize the uom
                .uom(new UnitOfMeasureCommand())
                .build()
        );
        model.addAttribute(ATTRIBUTE_UOM_LIST, unitOfMeasureService.listAllUoms());

        return VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM;
    }

    @GetMapping(RecipeController.URL_RECIPE + "/{recipeId}/ingredient/{id}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_INGREDIENT, ingredientService.findByRecipeIdByIngredientId(
                Long.valueOf(recipeId), Long.valueOf(id)
        ));
        model.addAttribute(ATTRIBUTE_UOM_LIST, unitOfMeasureService.listAllUoms());
        return VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM;
    }

    @PostMapping(RecipeController.URL_RECIPE + "/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("Saved recipe id = " + savedCommand.getRecipeId());
        log.debug("Saved ingredient id = " + savedCommand.getId());
        return "redirect:" + RecipeController.URL_RECIPE + "/" + savedCommand.getRecipeId() +
                "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping(RecipeController.URL_RECIPE + "/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        ingredientService.deleteByRecipeIdByIngredientId(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:" + RecipeController.URL_RECIPE + "/" + recipeId + "/ingredients";
    }
}
