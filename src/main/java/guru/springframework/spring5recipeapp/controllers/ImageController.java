package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.services.ImageService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static guru.springframework.spring5recipeapp.controllers.RecipeController.URL_RECIPE;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RequiredArgsConstructor
@Slf4j
@Controller
final class ImageController {
    static final String VIEW_IMAGE_UPLOAD_FORM = "recipe/imageuploadform";
    static final String URL_IMAGE = URL_RECIPE + "/{id}/image";
    static final String URL_RECIPE_IMAGE = URL_RECIPE + "/{id}/recipeimage";

    private final RecipeService recipeService;
    private final ImageService imageService;

    @GetMapping(URL_IMAGE)
    public String showImageUploadForm(@PathVariable String id, Model model) {
        model.addAttribute(RecipeController.ATTRIBUTE_RECIPE, recipeService.findCommandById(Long.valueOf(id)));
        return VIEW_IMAGE_UPLOAD_FORM;
    }

    @PostMapping(URL_IMAGE)
    public String uploadImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(id), file);
        return "redirect:" + URL_RECIPE + "/" + id + "/show";
    }

    @GetMapping(URL_RECIPE_IMAGE)
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

        if (recipeCommand.getImage() != null) {
            byte[] bytes = new byte[recipeCommand.getImage().length];
            for (int i = 0; i < recipeCommand.getImage().length; i++) {
                bytes[i] = recipeCommand.getImage()[i];
            }

            response.setContentType("image/jpeg");

            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                log.error("IOException occurred", e);
                throw new RuntimeException("IOException occurred", e);
            }
        }
    }
}
