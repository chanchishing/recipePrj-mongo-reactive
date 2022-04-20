package guru.springframework.controllers;


import guru.springframework.model.Recipe;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"","/","/index"})
    public String getIndexPage(Model model){

        Flux<Recipe> recipeList= recipeService.getRecipeList();

        model.addAttribute("recipes",recipeList);

        return "index";
    }
}
