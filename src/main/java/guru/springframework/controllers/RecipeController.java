package guru.springframework.controllers;


import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@Slf4j
@Controller
public class RecipeController {
    private static final String RECIPE_RECIPEFORM_URL="recipe/recipeform";

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping("/recipe/{id}/show")
    public String getRecipePage(@PathVariable String id,Model model){

        log.debug("Inside RecipeController.getRecipePage()"+id);

        Mono<Recipe> recipe=recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);

        return "/recipe/show";
    }


    @GetMapping("recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;

    }

    //@PostMapping("recipe")
    //public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){
    //    if (bindingResult.hasErrors()) {
    //        bindingResult.getAllErrors().forEach((error)->log.error(error.toString()));
    //
    //        //if  (command.getId()!=null) {
    //        //    RecipeCommand freshRecipeFromDB = recipeService.getRecipeCommandById(command.getId()).block();
    //        //    command.setIngredients(freshRecipeFromDB.getIngredients());
    //        //}
    //
    //        return RECIPE_RECIPEFORM_URL;
    //
    //    }
    //
    //    Mono<RecipeCommand> savedCommand=recipeService.saveRecipe(command);
    //    return("redirect:/recipe/"+savedCommand.block().getId()+"/show/");
    //
    //}

    @PostMapping("recipe")
    public Mono<String> saveOrUpdate(@Valid @ModelAttribute("recipe") Mono<RecipeCommand> command) {
        return command
                .flatMap(recipeService::saveRecipe)
                .map(recipe -> "redirect:/recipe/" + recipe.getId() + "/show")
                .doOnError(thr -> log.error("Error saving recipe"))
                .onErrorResume(WebExchangeBindException.class, thr -> Mono.just(RECIPE_RECIPEFORM_URL));
    }


    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id,Model model){
        Mono<RecipeCommand> recipeCommand=recipeService.getRecipeCommandById(id);
        model.addAttribute("recipe", recipeCommand);
        return RECIPE_RECIPEFORM_URL;
    }


    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id){
        recipeService.deleteRecipeById(id);
        //model.addAttribute("recipe", recipeCommand);
        return "redirect:/";
    }

}
