package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandTest {

    IngredientToIngredientCommand converter;


    final String testIngredientId ="1";
    final String testIngredientDescription ="ingredient Description";
    final BigDecimal testAmount = new BigDecimal(20);

    final String testUomId ="7";
    final String testUomDescription ="uom description";

    @BeforeEach
    void setUp() {
        converter=new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    void testConvertNull(){
        assertNull(converter.convert(null));
    }

    @Test
    void testConvertEmptyObject(){
        assertNotNull(converter.convert(new Ingredient()));
    }


    @Test
    void convert() {
        //given
        Ingredient testIngrdient;
        testIngrdient=new Ingredient();
        testIngrdient.setId(testIngredientId);
        testIngrdient.setDescription(testIngredientDescription);
        testIngrdient.setAmount(testAmount);

        UnitOfMeasure testUom;
        testUom=new UnitOfMeasure();
        testUom.setId(testUomId);
        testUom.setDescription(testUomDescription);


        testIngrdient.setUom(testUom);


        //when
        IngredientCommand testResult=converter.convert(testIngrdient);

        //then
        assertInstanceOf(IngredientCommand.class,testResult);
        assertEquals(testIngredientId,testResult.getId());
        assertEquals(testIngredientDescription,testResult.getDescription());
        assertEquals(testAmount,testResult.getAmount());
        assertEquals(testUomId,testResult.getUom().getId());
        assertEquals(testUomDescription,testResult.getUom().getDescription());


    }


}