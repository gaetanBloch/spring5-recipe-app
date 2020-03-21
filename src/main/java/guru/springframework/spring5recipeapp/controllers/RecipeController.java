package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping("/recipe/show/{id}")
    public String showById(@PathVariable String id, Model model) {

        model.addAttribute("recipe", recipeService.findById(new Long(id)));

        return "recipe/show";
    }
}
