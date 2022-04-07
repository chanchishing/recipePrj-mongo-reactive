package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {

    NotesToNotesCommand converter;
    Notes testNotes;
    final String testId="1";
    final String testRecipeNotes="Test Recipe Notes";


    @BeforeEach
    void setUp() {
        converter=new NotesToNotesCommand();
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    void convert() {
        //given
        testNotes=new Notes();
        testNotes.setId(testId);
        testNotes.setRecipeNotes(testRecipeNotes);

        //when
        NotesCommand testResult=converter.convert(testNotes);

        //then
        assertInstanceOf(NotesCommand.class,testResult);
        assertEquals(testId,testResult.getId());
        assertEquals(testRecipeNotes,testResult.getRecipeNotes());

    }


}