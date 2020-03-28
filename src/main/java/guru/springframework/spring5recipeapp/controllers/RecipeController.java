package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.CategoryService;
import guru.springframework.spring5recipeapp.services.RecipeService;
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
 * Created on 21/03/2020
 */
@Slf4j
@RequiredArgsConstructor
@Controller
final class RecipeController {
    static final String VIEW_RECIPE_SHOW_FORM = "recipe/show";
    static final String VIEW_RECIPE_CREATE_OR_UPDATE_FORM = "recipe/recipeform";
    static final String ATTRIBUTE_RECIPE = "recipe";
    static final String ATTRIBUTE_CATEGORIES = "categories";
    static final String URL_RECIPE = "/recipe";
    static final String URL_RECIPE_NEW = URL_RECIPE + "/new";
    static final String URL_RECIPE_SHOW = URL_RECIPE + "/{id}/show";
    static final String URL_RECIPE_UPDATE = URL_RECIPE + "/{id}/update";
    static final String URL_RECIPE_DELETE = URL_RECIPE + "/{id}/delete";

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    @GetMapping(URL_RECIPE_SHOW)
    public String showRecipe(@PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_RECIPE, recipeService.findById(Long.valueOf(id)));
        return VIEW_RECIPE_SHOW_FORM;
    }

    @GetMapping(URL_RECIPE_NEW)
    public String newRecipe(Model model) {
        model.addAttribute(ATTRIBUTE_RECIPE, new RecipeCommand());
        model.addAttribute(ATTRIBUTE_CATEGORIES, categoryService.findAll());
        return VIEW_RECIPE_CREATE_OR_UPDATE_FORM;
    }

    @GetMapping(URL_RECIPE_UPDATE)
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute(ATTRIBUTE_RECIPE, recipeService.findCommandById(Long.valueOf(id)));
        model.addAttribute(ATTRIBUTE_CATEGORIES, categoryService.findAll());
        return VIEW_RECIPE_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(URL_RECIPE)
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:" + URL_RECIPE + "/" + savedCommand.getId() + "/show";
    }

    @GetMapping(URL_RECIPE_DELETE)
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
}
