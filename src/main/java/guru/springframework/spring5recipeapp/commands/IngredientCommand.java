package guru.springframework.spring5recipeapp.commands;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Gaetan Bloch
 * Created on 21/03/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientCommand {
    private Long id;
    private Long recipeId;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureCommand uom;
}
