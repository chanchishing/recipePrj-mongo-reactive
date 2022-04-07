package guru.springframework.bootstrap;


import guru.springframework.model.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@Component
@Profile("default")
public class BootStrapDataDefault implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    Map<String, UnitOfMeasure >uomS = Map.ofEntries(
            entry("",new UnitOfMeasure()),
            entry("Teaspoon", new UnitOfMeasure()),
            entry("Tablespoon", new UnitOfMeasure()),
            entry("Pinch",new UnitOfMeasure()),
            entry("Cup",new UnitOfMeasure()),
            entry("Ounce",new UnitOfMeasure()),
            entry("Pint",new UnitOfMeasure()),
            entry("Dash",new UnitOfMeasure()));

    Map<String, Category >categoryS = Map.ofEntries(
            entry("American",new Category()),
            entry("Italian",new Category()),
            entry("Mexican",new Category()),
            entry("Fast Food",new Category())
    );

    public BootStrapDataDefault(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Transactional
    private void initUOMs() {

        if (unitOfMeasureRepository.count() == 0) {
            this.uomS.forEach((description, uomElement) -> {
                uomElement.setDescription(description);
                unitOfMeasureRepository.save(uomElement);
            });
        }
    }


    @Transactional
    private void initCategory() {

        if (categoryRepository.count() == 0) {
            categoryS.forEach((catDescription,category) -> {
                category.setDescription(catDescription);
                categoryRepository.save(category);
            });
        }
    }

    @Transactional
    private Recipe initGuacamole() {

        Recipe guacamole = new Recipe();

        guacamole.setDescription("Prefect Guacamole");
        guacamole.setCookTime(10);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setPrepTime(10);
        guacamole.setServings(4);
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.getCategories().add(categoryS.get("Mexican"));
        guacamole.getCategories().add(categoryS.get("American"));

        guacamole.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");

        Notes guacamoleNotes =new Notes();
        guacamoleNotes.setRecipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries.\n" +
                "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.\n");
        guacamole.setNotes(guacamoleNotes);


        guacamole.addIngredient(new Ingredient("avocado",new BigDecimal("2.0"),uomS.get("")));
        guacamole.addIngredient(new Ingredient("salt",new BigDecimal("0.25"),uomS.get("Teaspoon")));
        guacamole.addIngredient(new Ingredient("fresh lime or lemon juice",new BigDecimal("1.0"),uomS.get("Tablespoon")));
        guacamole.addIngredient(new Ingredient("minced red onion or thinly sliced green onion",new BigDecimal("4.0"),uomS.get("Tablespoon")));
        guacamole.addIngredient(new Ingredient("serrano chilis, stems and seeds removed, minced",new BigDecimal("2.0"),uomS.get("")));
        guacamole.addIngredient(new Ingredient("cilantro,finely chopped",new BigDecimal("2.0"),uomS.get("Tablespoon")));
        guacamole.addIngredient(new Ingredient("freshly ground black pepper",new BigDecimal("1.0"),uomS.get("Pinch")));
        guacamole.addIngredient(new Ingredient("ripe tomato, chopped",new BigDecimal("0.5"),uomS.get("")));
        guacamole.addIngredient(new Ingredient("Red radish or jicama slices",BigDecimal.ZERO,uomS.get("")));
        guacamole.addIngredient(new Ingredient("Tortilla chips",BigDecimal.ZERO,uomS.get("")));

        return guacamole;
    }

    @Transactional
    private Recipe initSpicyGCTacos() {
        Recipe spicyGCTacos = new Recipe();

        spicyGCTacos.setDescription("Spicy Grilled Chicken Tacos");
        spicyGCTacos.setCookTime(15);
        spicyGCTacos.setDifficulty(Difficulty.KIND_OF_HARD);
        spicyGCTacos.setPrepTime(20);
        spicyGCTacos.setServings(6);
        spicyGCTacos.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        spicyGCTacos.getCategories().add(categoryS.get("Mexican"));
        spicyGCTacos.getCategories().add(categoryS.get("American"));

        spicyGCTacos.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                                   "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                                   "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                                   "\n" +
                                   "\n" +
                                   "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                                   "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                                   "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                                   "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n");

        Notes spicyGCTacosNotes=new Notes();
        spicyGCTacosNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n");
        spicyGCTacos.setNotes(spicyGCTacosNotes);


        spicyGCTacos.addIngredient(new Ingredient("ancho chili powder",new BigDecimal("2.0"),uomS.get("Tablespoon")));
        spicyGCTacos.addIngredient(new Ingredient("dired oregano",new BigDecimal("1.0"),uomS.get("Teaspoon")));
        spicyGCTacos.addIngredient(new Ingredient("dired cumin",new BigDecimal("1.0"),uomS.get("Teaspoon")));
        spicyGCTacos.addIngredient(new Ingredient("sugar",new BigDecimal("1.0"),uomS.get("Teaspoon")));
        spicyGCTacos.addIngredient(new Ingredient("salt",new BigDecimal("0.5"),uomS.get("Teaspoon")));
        spicyGCTacos.addIngredient(new Ingredient("clove garlic",new BigDecimal("1.0"),uomS.get("")));
        spicyGCTacos.addIngredient(new Ingredient("orange zest",new BigDecimal("1.0"),uomS.get("Tablespoon")));
        spicyGCTacos.addIngredient(new Ingredient("small tortillas",new BigDecimal("8.0"),uomS.get("")));
        spicyGCTacos.addIngredient(new Ingredient("packed baby arugula",new BigDecimal("3.0"),uomS.get("Ounce")));
        spicyGCTacos.addIngredient(new Ingredient("medium ripe avocado",new BigDecimal("2.0"),uomS.get("Ounce")));
        spicyGCTacos.addIngredient(new Ingredient("radish thinly sliced",new BigDecimal("4.0"),uomS.get("")));
        spicyGCTacos.addIngredient(new Ingredient("cherry tomato halved",new BigDecimal("0.5"),uomS.get("Pint")));
        spicyGCTacos.addIngredient(new Ingredient("red onion",new BigDecimal("0.25"),uomS.get("")));
        spicyGCTacos.addIngredient(new Ingredient("roughly chopped cilantro",BigDecimal.ZERO,uomS.get("")));
        spicyGCTacos.addIngredient(new Ingredient("sour cream",new BigDecimal("0.5"),uomS.get("Cup")));
        spicyGCTacos.addIngredient(new Ingredient("milk",new BigDecimal("0.25"),uomS.get("Cup")));
        spicyGCTacos.addIngredient(new Ingredient("lime wedges",new BigDecimal("1.0"),uomS.get("")));

        return spicyGCTacos;
    }

    private void doDBInit() {
        initUOMs();
        initCategory();
        recipeRepository.save(initGuacamole());
        recipeRepository.save(initSpicyGCTacos());
        log.error("### Play with Mongo reactive driver ###");
        log.error("Count uom using reactive Mongo Driver:"+unitOfMeasureReactiveRepository.count().block().toString());
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Started in Bootstrap");
        doDBInit();
        log.info("Leaving Bootstrap");

    }
}
