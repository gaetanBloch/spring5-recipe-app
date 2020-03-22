package guru.springframework.spring5recipeapp.converters;

import guru.springframework.spring5recipeapp.commands.NotesCommand;
import guru.springframework.spring5recipeapp.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static guru.springframework.spring5recipeapp.TestUtils.ID;
import static guru.springframework.spring5recipeapp.TestUtils.RECIPE_NOTES;
import static org.junit.Assert.*;

/**
 * @author Gaetan Bloch
 * Created on 22/03/2020
 */
public class NotesToNotesCommandTest {
    private NotesToNotesCommand converter;

    @Before
    public void setUp() {
        converter = new NotesToNotesCommand();
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    public void convertTest() {
        // Given
        Notes notes = Notes.builder()
                .recipeNotes(RECIPE_NOTES)
                .id(ID)
                .build();
        NotesCommand notesCommand;

        // When
        notesCommand = converter.convert(notes);

        // Then
        assertNotNull(notesCommand);
        assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
        assertEquals(ID, notesCommand.getId());
    }
}