package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    IngredientCommandToIngredient converter;


    final String testIngredientId ="1";
    final String testIngredientDescription ="ingredient Description";
    final BigDecimal testAmount = new BigDecimal(20);

    final String testUomId ="7";
    final String testUomDescription ="uom description";

    @BeforeEach
    void setUp() {
        converter=new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new IngredientCommand()));
    }


    @Test
    void convert() {
        //given
        IngredientCommand testCommand;
        testCommand=new IngredientCommand();
        testCommand.setId(testIngredientId);
        testCommand.setDescription(testIngredientDescription);
        testCommand.setAmount(testAmount);

        UnitOfMeasureCommand testUomCommand;
        testUomCommand=new UnitOfMeasureCommand();
        testUomCommand.setId(testUomId);
        testUomCommand.setDescription(testUomDescription);


        testCommand.setUom(testUomCommand);


        //when
        Ingredient testResult=converter.convert(testCommand);

        //then
        assertInstanceOf(Ingredient.class,testResult);
        assertEquals(testIngredientId,testResult.getId());
        assertEquals(testIngredientDescription,testResult.getDescription());
        assertEquals(testAmount,testResult.getAmount());
        assertEquals(testUomId,testResult.getUom().getId());
        assertEquals(testUomDescription,testResult.getUom().getDescription());


    }


}