package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeToRecipeCommand recipeToCommand;
    private final RecipeCommandToRecipe commandToRecipe;



    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                             RecipeToRecipeCommand recipeToCommand,
                             RecipeCommandToRecipe commandToRecipe) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeToCommand = recipeToCommand;
        this.commandToRecipe = commandToRecipe;
    }

    @Override
    public Flux<Recipe> getRecipeList(){
        log.debug("I'm RecipeServiceImpl.getRecipeList()");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> getRecipe(String id) {

        ////return recipeRepository.findById(id).orElseThrow();
        //return recipeRepository.findById(id).orElseThrow(()->
        //        {
        //            return new NotFoundException("Recipe not found for id:"+String.valueOf(id));
        //        }
        //    );
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> saveRecipe(RecipeCommand command) {

        Recipe recipe=commandToRecipe.convert(command);
        if (recipe.getId().isEmpty()) {
            recipe.setId(null);
        }

        Mono<Recipe> monoRecipe=recipeReactiveRepository.save(recipe);

        return monoRecipe.map(savedRecipe-> {
            return recipeToCommand.convert(savedRecipe);
        });

    }

    @Override
    public Mono<RecipeCommand> getRecipeCommandById(String id) {
        return this.getRecipe(id).map(recipeToCommand::convert);
    }


    @Override
    public Mono<Void> deleteRecipeById(String id) {
        recipeReactiveRepository.deleteById(id).block();
        return Mono.empty();
    }

}
