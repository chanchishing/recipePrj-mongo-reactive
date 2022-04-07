package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.Recipe;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@Controller
public class IngredientController {

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
        RecipeCommand recipeCommand=recipeService.getRecipeCommandById(id);
        model.addAttribute("recipe", recipeCommand);
        return "/recipe/ingredient/list";
    }


    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute(
                "ingredient",
                ingredientService.findByRecipeIDAndIngredientId(recipeId,ingredientId)
        );

        return "/recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    public String loadIngredientToUpdate(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute(
                "ingredient",
                ingredientService.findByRecipeIDAndIngredientId(recipeId,ingredientId)
        );
        model.addAttribute(
                "uomList",
                unitOfMeasureService.getUomList()
        );

        return "/recipe/ingredient/ingredientForm";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String updateIngredient(@ModelAttribute IngredientCommand command){
        IngredientCommand savedIngredientCommand=ingredientService.saveIngredient(command);

        return "redirect:/recipe/" + savedIngredientCommand.getRecipeId() + "/ingredients/" + savedIngredientCommand.getId() + "/show" ;

    }


    @GetMapping("/recipe/{recipeId}/ingredients/new")
    public String loadIngredientFormToAdd(@PathVariable String recipeId, Model model) {

        try {
            Recipe recipe = recipeService.getRecipe(recipeId);
        } catch (NoSuchElementException noSuchElementException) {
            log.info("Recipe Not Found");
            throw noSuchElementException;
        } catch (Exception e) {
            throw e;
        }

        IngredientCommand ingredientCommand=new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.getUomList());

        return "/recipe/ingredient/ingredientForm";
    }


    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model){

        ingredientService.deleteAnIngredient(recipeId, ingredientId);

        return "redirect:/recipe/" + recipeId + "/ingredients/";
    }

}
