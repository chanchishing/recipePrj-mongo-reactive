package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIDAndIngredientId(String recipeId, String ingredientId);

    IngredientCommand saveIngredient(IngredientCommand ingredientCommand);

    void deleteAnIngredient(String recipeId, String ingredientId);
}
