package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureToUnitOfMeasureCommandTest {

    UnitOfMeasureToUnitOfMeasureCommand converter;
    UnitOfMeasure testUom;
    final String testId="1";
    final String testDescription="Test Description";

    @BeforeEach
    void setUp() {
        converter=new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    void convert() {
        //given
        testUom=new UnitOfMeasure();
        testUom.setId(testId);
        testUom.setDescription(testDescription);

        //when
        UnitOfMeasureCommand testResultUomCommand=converter.convert(testUom);

        //then
        assertInstanceOf(UnitOfMeasureCommand.class,testResultUomCommand);
        assertEquals(testId,testResultUomCommand.getId());
        assertEquals(testDescription,testResultUomCommand.getDescription());

    }


}