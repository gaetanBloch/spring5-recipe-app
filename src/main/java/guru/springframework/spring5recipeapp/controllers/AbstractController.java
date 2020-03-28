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
    static final String VIEW_400_BAD_REQUEST = "400error";
    static final String ATTRIBUTE_EXCEPTION = "exception";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception exception) {
        return getModelAndView("Handling not found exception", VIEW_404_NOT_FOUND, exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception exception) {
        return getModelAndView("Handling number format exception", VIEW_400_BAD_REQUEST, exception);
    }

    private ModelAndView getModelAndView(String logMessage, String view, Exception exception) {
        log.error(logMessage);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(view);
        modelAndView.addObject(ATTRIBUTE_EXCEPTION, exception);
        return modelAndView;
    }
}
