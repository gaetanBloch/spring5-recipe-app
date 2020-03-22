package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
final class IndexController {
    private final RecipeService recipeService;

    @GetMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        log.debug("Loading Index Page...");
        model.addAttribute("recipes", recipeService.getRecipes());
        log.debug("Loading Index Page... OK");
        return "index";
    }
}
