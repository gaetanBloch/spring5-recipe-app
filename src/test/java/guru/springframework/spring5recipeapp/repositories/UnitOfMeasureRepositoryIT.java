package guru.springframework.spring5recipeapp.repositories;

import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryIT {
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() {
    }

    @Test
    public void findByDescriptionTeaspoonTest() {
        // Given
        final String teaspoon = "Teaspoon";

        // When
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByDescription(teaspoon);

        // Then
        assertTrue(unitOfMeasure.isPresent());
        assertEquals(teaspoon, unitOfMeasure.get().getDescription());
    }

    @Test
    public void findByDescriptionCupTest() {
        // Given
        final String cup = "Cup";

        // When
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByDescription(cup);

        // Then
        assertTrue(unitOfMeasure.isPresent());
        assertEquals(cup, unitOfMeasure.get().getDescription());
    }
}