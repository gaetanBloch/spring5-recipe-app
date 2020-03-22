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
public class NotesCommandToNotesTest {
    private NotesCommandToNotes converter;

    @Before
    public void setUp() {
        converter = new NotesCommandToNotes();
    }

    @Test
    public void nullObjectTest() {
        assertNull(converter.convert(null));
    }

    @Test
    public void emptyObjectTest() {
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    public void convertTest() {
        // Given
        NotesCommand notesCommand = NotesCommand.builder()
                .recipeNotes(RECIPE_NOTES)
                .id(ID)
                .build();
        Notes notes;

        // When
        notes = converter.convert(notesCommand);

        // Then
        assertNotNull(notes);
        assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
        assertEquals(ID, notes.getId());
    }
}