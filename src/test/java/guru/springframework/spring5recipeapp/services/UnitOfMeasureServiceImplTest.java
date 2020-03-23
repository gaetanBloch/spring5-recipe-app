package guru.springframework.spring5recipeapp.services;

import com.google.common.collect.ImmutableSet;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.TestUtils.ID2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Gaetan Bloch
 * Created on 23/03/2020
 */
@RunWith(MockitoJUnitRunner.class)
public class UnitOfMeasureServiceImplTest {
    private UnitOfMeasureService unitOfMeasureService;
    @Mock
    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() {
        unitOfMeasureService = new UnitOfMeasureServiceImpl(
                unitOfMeasureRepository,
                unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUomsTest() {
        // Given
        Set<UnitOfMeasure> unitOfMeasures = ImmutableSet.of(
                UnitOfMeasure.builder().id(ID).build(),
                UnitOfMeasure.builder().id(ID2).build()
        );
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        when(unitOfMeasureToUnitOfMeasureCommand.convert(any()))
                .thenReturn(UnitOfMeasureCommand.builder().id(ID).build())
                .thenReturn(UnitOfMeasureCommand.builder().id(ID2).build());

        // When
        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUoms();

        // Then
        assertNotNull(commands);
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository).findAll();
        verify(unitOfMeasureToUnitOfMeasureCommand, times(2)).convert(any());
    }
}