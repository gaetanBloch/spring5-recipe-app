package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gaetan Bloch
 * Created on 28/03/2020
 */
@Slf4j
class AbstractController {
    static final String VIEW_404_NOT_FOUND = "404error";
    static final String ATTRIBUTE_EXCEPTION = "exception";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling not found exception");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(VIEW_404_NOT_FOUND);
        modelAndView.addObject(ATTRIBUTE_EXCEPTION, exception);
        return modelAndView;
    }
}
