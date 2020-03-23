package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;

import java.util.Set;

/**
 * @author Gaetan Bloch
 * Created on 23/03/2020
 */
public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
