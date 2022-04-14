package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {

    Flux<Recipe> getRecipeList();

    Mono<Recipe> getRecipe(String id);

    Mono<RecipeCommand> saveRecipe(RecipeCommand command);

    Mono<RecipeCommand> getRecipeCommandById(String id);

    Mono<Void> deleteRecipeById(String id);

}
