package guru.springframework.controllers;


import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

        Recipe recipe=recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);

        return "/recipe/show";
    }


    @GetMapping("recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;

    }

    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach((error)->log.error(error.toString()));

            if  (command.getId()!=null) {
                RecipeCommand freshRecipeFromDB = recipeService.getRecipeCommandById(command.getId());
                command.setIngredients(freshRecipeFromDB.getIngredients());
            }

            return RECIPE_RECIPEFORM_URL;

        }

        RecipeCommand savedCommand=recipeService.saveRecipe(command);
        return("redirect:/recipe/"+savedCommand.getId()+"/show/");

    }


    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id,Model model){
        RecipeCommand recipeCommand=recipeService.getRecipeCommandById(id);
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
