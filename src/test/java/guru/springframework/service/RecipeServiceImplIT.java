package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecipeServiceImplIT {

    static final String newRecipeDescription="New Description";

    @Autowired
    RecipeServiceImpl recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeToRecipeCommand recipeToCommand;

    @Autowired
    RecipeCommandToRecipe commandToRecipe;


    @Test
    void saveRecipe() {
        Flux<Recipe> recipeList=recipeService.getRecipeList();
        Recipe tstRecipe=recipeList.blockFirst();
        RecipeCommand command=recipeToCommand.convert((tstRecipe));

        command.setDescription(newRecipeDescription);
        RecipeCommand savedCommand=recipeService.saveRecipe(command).block();

        assertEquals(newRecipeDescription,savedCommand.getDescription());
        assertEquals(command.getId(),savedCommand.getId());
        assertEquals(command.getCookTime(),savedCommand.getCookTime());
        assertEquals(command.getPrepTime(),savedCommand.getPrepTime());
        assertEquals(command.getDifficulty(),savedCommand.getDifficulty());
        assertEquals(command.getIngredients().size(),savedCommand.getIngredients().size());


    }
}