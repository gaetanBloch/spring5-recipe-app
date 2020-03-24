package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.services.ImageService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RequiredArgsConstructor
@Controller
final class ImageController {
    private final RecipeService recipeService;
    private final ImageService imageService;

    @GetMapping("/recipe/{id}/image")
    public String showImageUploadForm(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
            return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{id}/image")
    public String uploadImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(id), file);
        return "redirect:/recipe/" + id + "/show";
    }
}
