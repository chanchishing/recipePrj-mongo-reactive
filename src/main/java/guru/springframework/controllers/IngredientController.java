package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
@Controller
public class IngredientController {

    public static final String RECIPE_INGREDIENTFORM_URL = "/recipe/ingredient/ingredientForm";
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;


    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model){
        Mono<RecipeCommand> recipeCommand=recipeService.getRecipeCommandById(id);
        model.addAttribute("recipe", recipeCommand);
        return "/recipe/ingredient/list";
    }


    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        Mono<IngredientCommand> ingredientCommandMono=ingredientService.findByRecipeIDAndIngredientId(recipeId,ingredientId).flatMap(ingredientCommand -> {
            ingredientCommand.setRecipeId(recipeId);
            return Mono.just(ingredientCommand);
        });

        //ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommandMono);

        return "/recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    public String loadIngredientToUpdate(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        Mono<IngredientCommand> ingredientCommand=ingredientService.findByRecipeIDAndIngredientId(recipeId,ingredientId).flatMap((ingredient)->{
            ingredient.setRecipeId(recipeId);
            return Mono.just(ingredient);
        });

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.getUomList().collectList());

        return RECIPE_INGREDIENTFORM_URL;
    }


    @PostMapping("/recipe/{recipeId}/ingredient")
    public Mono<String> updateIngredient(@Valid @ModelAttribute Mono<IngredientCommand> command){
        //Mono<IngredientCommand> savedIngredientCommand=ingredientService.saveIngredient(command);

        //return "redirect:/recipe/" + savedIngredientCommand.getRecipeId() + "/ingredients/" + savedIngredientCommand.getId() + "/show" ;

        return command
                .flatMap(ingredientService::saveIngredient)
                .map(savedIngredientCommand -> "redirect:/recipe/" + savedIngredientCommand.getRecipeId() + "/ingredients/" + savedIngredientCommand.getId() + "/show")
                .doOnError(thr -> log.error("Error saving Ingredient"))
                .onErrorResume(WebExchangeBindException.class, thr -> Mono.just(RECIPE_INGREDIENTFORM_URL));

    }


    @GetMapping("/recipe/{recipeId}/ingredients/new")
    public String loadIngredientFormToAdd(@PathVariable String recipeId, Model model) {

        IngredientCommand ingredientCommand=new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        Flux<UnitOfMeasureCommand> unitOfMeasureCommandFlux=recipeService.getRecipe(recipeId).flatMapMany((recipe)->{
            if (recipe.equals(Mono.empty())) {
                log.info("Recipe Not Found Id:"+ recipeId);
                throw new NoSuchElementException("Recipe of Id:" + recipeId + " not found");
            }
            return unitOfMeasureService.getUomList();
        });

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureCommandFlux);

        return RECIPE_INGREDIENTFORM_URL;
    }


    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model){

        ingredientService.deleteAnIngredient(recipeId, ingredientId);

        return "redirect:/recipe/" + recipeId + "/ingredients/";
    }

}
