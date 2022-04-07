package guru.springframework.controllers;


import guru.springframework.model.Recipe;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"","/","/index"})
    public String getIndexPage(Model model){

        List<Recipe> recipeList=recipeService.getRecipeList();

        //System.out.println("Number of Recipe:" + recipeList.size());
        log.debug("Number of Recipe:" + recipeList.size());
        model.addAttribute("recipes",recipeList);

        return "index";
    }
}
