package guru.springframework.service;


import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository uomReactiveRepository;
    private final IngredientToIngredientCommand ingredientToCommand;
    private final IngredientCommandToIngredient commandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository uomReactiveRepository, IngredientToIngredientCommand ingredientToCommand, IngredientCommandToIngredient commandToIngredient) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.uomReactiveRepository = uomReactiveRepository;
        this.ingredientToCommand = ingredientToCommand;
        this.commandToIngredient = commandToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIDAndIngredientId(String recipeId, String ingredientId) {

        Mono<Recipe> monoRecipe =recipeReactiveRepository.findById(recipeId);

        return monoRecipe.map(recipe-> {
            Set<Ingredient> ingredientSet=recipe.getIngredients();
            Ingredient ingredient=ingredientSet.stream().filter(element->element.getId().equals(ingredientId)).findFirst().orElseThrow();
            return ingredientToCommand.convert(ingredient);
        });



    }

    @Override
    public Mono<Void> deleteAnIngredient(String recipeId, String ingredientId) {

        Recipe recipe;

        //Check recipe of Ingredient is an existing recipe
        try {
            recipe = recipeReactiveRepository.findById(recipeId).blockOptional().orElseThrow();
        } catch (NoSuchElementException noElement) {
            log.error("Recipe Not Found");
            throw noElement;
        } catch (Exception e){
            throw e;
        }

        recipe.getIngredients().stream().filter(element->element.getId().equals(ingredientId)).findFirst().ifPresentOrElse(
                //ingredient is an existing ingredient of recipe,remove it
                (ingredient) -> {
                    //ingredient.setRecipe(null);
                    recipe.getIngredients().remove(ingredient);
                },
                //ingredient is not an existing ingredient, cannot delete
                () ->{
                    log.error("Ingredient is not existing ingredient");
                    throw new NoSuchElementException();
                }
        );

        Recipe savedRecipe=recipeReactiveRepository.save(recipe).block();

        return Mono.empty();

    }

    @Override
    public Mono<IngredientCommand> saveIngredient(IngredientCommand ingredientCommand) {
        Recipe recipe;

        //Check recipe of Ingredient is an existing recipe
        try {
            recipe = recipeReactiveRepository.findById(ingredientCommand.getRecipeId()).blockOptional().orElseThrow();
        } catch (NoSuchElementException noElement) {
            log.error("Recipe Not Found");
            throw noElement;
        } catch (Exception e){
            throw e;
        }

        recipe.getIngredients().stream().filter(element->element.getId().equals(ingredientCommand.getId())).findFirst().ifPresentOrElse(
                //ingredient is an existing ingredient of recipe
                (ingredient) -> {
                    ingredient.setDescription(ingredientCommand.getDescription());
                    ingredient.setAmount(ingredientCommand.getAmount());
                    try { //make sure uom already exists in DB
                        ingredient.setUom(uomReactiveRepository.findById(ingredientCommand.getUom().getId()).blockOptional().orElseThrow());
                    } catch (NoSuchElementException noElement){
                        log.error("Unit of Measure not found");
                        throw noElement;
                    } catch (Exception e){
                        throw e;
                    }},
                //ingredient is not an existing ingredient, also need to check uom already exist
                () ->{
                    try { //make sure uom already exists in DB
                        uomReactiveRepository.findById(ingredientCommand.getUom().getId()).blockOptional().orElseThrow();
                    } catch (NoSuchElementException noElement){
                        log.error("Unit of Measure not found");
                        throw noElement;
                    } catch (Exception e){
                        throw e;
                    }
                    ingredientCommand.setId(UUID.randomUUID().toString());
                    recipe.addIngredient(commandToIngredient.convert(ingredientCommand));
                }
        );

        Mono<Recipe> savedRecipeMono=recipeReactiveRepository.save(recipe);

        return savedRecipeMono.map(savedRecipe-> {
            Set<Ingredient> ingredientSet=savedRecipe.getIngredients();
            Ingredient ingredient=ingredientSet.stream().filter(element->element.getId().equals(ingredientCommand.getId())).findFirst().orElseThrow();
            IngredientCommand savedIngredientCommand =ingredientToCommand.convert(ingredient);
            savedIngredientCommand.setRecipeId(ingredientCommand.getRecipeId());
            return savedIngredientCommand;
        });
    }
}
