package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {

    CategoryToCategoryCommand converter;
    Category testCategory;
    final String testId="1";
    final String testDescription="Test Description";

    @BeforeEach
    void setUp() {
        converter=new CategoryToCategoryCommand();
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    void convert() {
        //given
        testCategory=new Category();
        testCategory.setId(testId);
        testCategory.setDescription(testDescription);

        //when
        CategoryCommand testResult=converter.convert(testCategory);

        //then
        assertInstanceOf(CategoryCommand.class,testResult);
        assertEquals(testId,testResult.getId());
        assertEquals(testDescription,testResult.getDescription());

    }
}