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
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe id = " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdByIngredientId(
                Long.valueOf(recipeId), Long.valueOf(id)
        ));
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        // Throws Exception if the recipe id is not found (Verification that the recipe exists)
        recipeService.findCommandById(Long.valueOf(recipeId));

        model.addAttribute("ingredient", IngredientCommand.builder()
                // We need to put the recipe id for the POST url form
                .recipeId(Long.valueOf(recipeId))
                // We need to initialize the uom
                .uom(new UnitOfMeasureCommand())
                .build()
        );
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdByIngredientId(
                Long.valueOf(recipeId), Long.valueOf(id)
        ));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("Saved recipe id = " + savedCommand.getRecipeId());
        log.debug("Saved ingredient id = " + savedCommand.getId());
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        ingredientService.deleteByRecipeIdByIngredientId(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
