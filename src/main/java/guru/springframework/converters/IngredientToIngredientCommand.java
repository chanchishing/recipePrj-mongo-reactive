package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }



    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {
        if (source==null) {
            return null;
        };

        final IngredientCommand ingredientCommand=new IngredientCommand();
        ingredientCommand.setId(source.getId());
        //if (source.getRecipe()!=null){
        //    ingredientCommand.setRecipeId(source.getRecipe().getId());
        //}
        ingredientCommand.setDescription(source.getDescription());
        ingredientCommand.setAmount(source.getAmount());
        ingredientCommand.setUom(unitOfMeasureToUnitOfMeasureCommand.convert(source.getUom()));
        return ingredientCommand;
    }
}
