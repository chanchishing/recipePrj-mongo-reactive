package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToCommand;
    private final RecipeCommandToRecipe commandToRecipe;



    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeToRecipeCommand recipeToCommand,
                             RecipeCommandToRecipe commandToRecipe) {
        this.recipeRepository = recipeRepository;
        this.recipeToCommand = recipeToCommand;
        this.commandToRecipe = commandToRecipe;
    }

    @Override
    public List<Recipe> getRecipeList(){
        log.debug("I'm RecipeServiceImpl.getRecipeList()");
        return (List<Recipe>) recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipe(String id) {

        //return recipeRepository.findById(id).orElseThrow();
        return recipeRepository.findById(id).orElseThrow(()->
                {
                    return new NotFoundException("Recipe not found for id:"+String.valueOf(id));
                }
            );
    }

    @Transactional
    @Override
    public RecipeCommand saveRecipe(RecipeCommand command) {
        Recipe recipe=commandToRecipe.convert(command);
        Recipe savedRecipe=recipeRepository.save(recipe);
        return recipeToCommand.convert(savedRecipe);
    }

    @Transactional
    @Override
    public RecipeCommand getRecipeCommandById(String id) {
        return recipeToCommand.convert(this.getRecipe(id));
    }

    @Transactional
    @Override
    public void deleteRecipeById(String id) {
        recipeRepository.deleteById(id);
    }

}
