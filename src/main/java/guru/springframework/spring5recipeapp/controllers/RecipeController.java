package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Controller
@RequiredArgsConstructor
final class RecipeController {
    static final String VIEWS_RECIPE_SHOW_FORM = "recipe/show";
    static final String VIEWS_RECIPE_CREATE_OR_UPDATE_FORM = "recipe/recipeform";
    static final String ATTRIBUTE_RECIPE = "recipe";
    static final String URL_RECIPE = "/recipe";
    static final String URL_RECIPE_NEW = URL_RECIPE + "/new";

    private final RecipeService recipeService;

    @GetMapping(URL_RECIPE + "/{id}/show")
    public String showRecipe(@PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_RECIPE, recipeService.findById(Long.valueOf(id)));
        return VIEWS_RECIPE_SHOW_FORM;
    }

    @GetMapping(URL_RECIPE_NEW)
    public String newRecipe(Model model) {
        model.addAttribute(ATTRIBUTE_RECIPE, new RecipeCommand());
        return VIEWS_RECIPE_CREATE_OR_UPDATE_FORM;
    }

    @GetMapping(URL_RECIPE + "/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_RECIPE, recipeService.findCommandById(Long.valueOf(id)));
        return VIEWS_RECIPE_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(URL_RECIPE)
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:" + URL_RECIPE + "/" + savedCommand.getId() + "/show";
    }

    @GetMapping(URL_RECIPE + "/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
}
