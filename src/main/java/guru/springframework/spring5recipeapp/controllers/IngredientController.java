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

import static guru.springframework.spring5recipeapp.controllers.RecipeController.URL_RECIPE;

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
    static final String URL_INGREDIENT = URL_RECIPE + "/{recipeId}/ingredient";
    static final String URL_INGREDIENTS = URL_RECIPE + "/{recipeId}/ingredients";
    static final String URL_INGREDIENT_SHOW = URL_RECIPE + "/{recipeId}/ingredient/{id}/show";
    static final String URL_INGREDIENT_NEW = URL_RECIPE + "/{recipeId}/ingredient/{id}/new";
    static final String URL_INGREDIENT_UPDATE = URL_RECIPE + "/{recipeId}/ingredient/{id}/update";
    static final String URL_INGREDIENT_DELETE = URL_RECIPE + "/{recipeId}/ingredient/{id}/delete";
    static final String ATTRIBUTE_INGREDIENT = "ingredient";
    static final String ATTRIBUTE_UOM_LIST = "uomList";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping(URL_INGREDIENTS)
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe id = " + recipeId);
        model.addAttribute(RecipeController.ATTRIBUTE_RECIPE, recipeService.findCommandById(Long.valueOf(recipeId)));
        return VIEW_INGREDIENT_LIST;
    }

    @GetMapping(URL_INGREDIENT_SHOW)
    public String showIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_INGREDIENT, ingredientService.findByRecipeIdByIngredientId(
                Long.valueOf(recipeId), Long.valueOf(id)
        ));
        return VIEW_INGREDIENT_SHOW;
    }

    @GetMapping(URL_INGREDIENT_NEW)
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

    @GetMapping(URL_INGREDIENT_UPDATE)
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_INGREDIENT, ingredientService.findByRecipeIdByIngredientId(
                Long.valueOf(recipeId), Long.valueOf(id)
        ));
        model.addAttribute(ATTRIBUTE_UOM_LIST, unitOfMeasureService.listAllUoms());
        return VIEW_CREATE_OR_UPDATE_INGREDIENT_FORM;
    }

    @PostMapping(URL_INGREDIENT)
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("Saved recipe id = " + savedCommand.getRecipeId());
        log.debug("Saved ingredient id = " + savedCommand.getId());
        return "redirect:" + URL_RECIPE + "/" + savedCommand.getRecipeId() +
                "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping(URL_INGREDIENT_DELETE)
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        ingredientService.deleteByRecipeIdByIngredientId(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:" + URL_RECIPE + "/" + recipeId + "/ingredients";
    }
}
