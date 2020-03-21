package guru.springframework.spring5recipeapp.commands;

import guru.springframework.spring5recipeapp.domain.Difficulty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCommand {
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
}
