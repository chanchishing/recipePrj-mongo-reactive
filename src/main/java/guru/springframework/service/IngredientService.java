package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

    Mono<IngredientCommand> findByRecipeIDAndIngredientId(String recipeId, String ingredientId);

    Mono<IngredientCommand> saveIngredient(IngredientCommand ingredientCommand);

    void deleteAnIngredient(String recipeId, String ingredientId);
}
