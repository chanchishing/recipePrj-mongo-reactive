package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.Category;
import guru.springframework.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

    CategoryCommandToCategory converter;
    CategoryCommand testCommand;
    final String testId="1";
    final String testDescription="Test Description";

    @BeforeEach
    void setUp() {
        converter=new CategoryCommandToCategory();
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        //given
        testCommand=new CategoryCommand();
        testCommand.setId(testId);
        testCommand.setDescription(testDescription);

        //when
        Category testResult=converter.convert(testCommand);

        //then
        assertInstanceOf(Category.class,testResult);
        assertEquals(testId,testResult.getId());
        assertEquals(testDescription,testResult.getDescription());

    }
}