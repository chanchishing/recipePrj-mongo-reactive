package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {

    UnitOfMeasureCommandToUnitOfMeasure converter;
    UnitOfMeasureCommand testCommand;
    final String testId="1";
    final String testDescription="Test Description";


    @BeforeEach
    void setUp() {
        converter=new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }


    @Test
    void convert() {
        //given
        testCommand=new UnitOfMeasureCommand();
        testCommand.setId(testId);
        testCommand.setDescription(testDescription);

        //when
        UnitOfMeasure testResultUOM=converter.convert(testCommand);

        //then
        assertInstanceOf(UnitOfMeasure.class,testResultUOM);
        assertEquals(testId,testResultUOM.getId());
        assertEquals(testDescription,testResultUOM.getDescription());

    }
}