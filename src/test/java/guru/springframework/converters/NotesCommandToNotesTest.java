package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesTest {

    NotesCommandToNotes converter;
    NotesCommand testCommand;
    final String testId="1";
    final String testRecipeNotes="Test Recipe Notes";


    @BeforeEach
    void setUp() {
        converter=new NotesCommandToNotes();
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    void convert() {
        //given
        testCommand=new NotesCommand();
        testCommand.setId(testId);
        testCommand.setRecipeNotes(testRecipeNotes);

        //when
        Notes testResult=converter.convert(testCommand);

        //then
        assertInstanceOf(Notes.class,testResult);
        assertEquals(testId,testResult.getId());
        assertEquals(testRecipeNotes,testResult.getRecipeNotes());

    }


}