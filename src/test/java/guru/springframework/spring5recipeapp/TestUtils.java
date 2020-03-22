package guru.springframework.spring5recipeapp;

import guru.springframework.spring5recipeapp.domain.Difficulty;

import java.math.BigDecimal;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public final class TestUtils {
    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final BigDecimal AMOUNT =  BigDecimal.valueOf(1);
    public static final Long UOM_ID = 2L;
    public static final String RECIPE_NOTES = "Notes";
    public static final Integer COOK_TIME = 5;
    public static final Integer PREP_TIME = 7;
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = 3;
    public static final String SOURCE = "Source";
    public static final String URL = "URL";
    public static final Long CAT_ID_1 = 1L;
    public static final Long CAT_ID_2 = 2L;
    public static final Long INGREDIENT_ID_1 = 3L;
    public static final Long INGREDIENT_ID_2 = 4L;
    public static final Long NOTES_ID = 9L;

    private TestUtils() {
        // To prevent instantiation
        throw new UnsupportedOperationException();
    }
}
