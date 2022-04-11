package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeReactiveRepository mockRecipeReactiveRepository;

    @Mock
    UnitOfMeasureReactiveRepository mockUomReactiveRepository;

    IngredientToIngredientCommand IngredientToCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    IngredientCommandToIngredient commandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(mockRecipeReactiveRepository, mockUomReactiveRepository, IngredientToCommand, commandToIngredient);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findByRecipeIDAndIngredientId() {
        String testRecipeId = "1";
        String testIngredientId = "2";

        Recipe recipe = new Recipe();
        recipe.setId(testRecipeId);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(testIngredientId);
        //ingredient.setRecipe(recipe);
        recipe.getIngredients().add(ingredient);
        //Optional<Recipe> recipeOptional = Optional.of(recipe);
        Mono<Recipe> recipeMono = Mono.just(recipe);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(testRecipeId);
        ingredientCommand.setId(testIngredientId);

        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);

        IngredientCommand command = ingredientService.findByRecipeIDAndIngredientId(testRecipeId, testIngredientId).block();
        verify(mockRecipeReactiveRepository, times(1)).findById(testRecipeId);
        //assertEquals(testRecipeId, command.getRecipeId());
        assertEquals(testIngredientId, command.getId());


    }

    @Test
    void saveExistingIngredient() {

        String testRecipeId = "11";
        String testIngredientId = "3";
        String testUOMId = "5";
        BigDecimal testIngredientAmount = BigDecimal.valueOf(2.0);
        String testIngredientDescription = "test Ingredient Description";
        String b4IngredientDescription = "b4 Ingredient Description";

        Mono<UnitOfMeasure> uomMono= Mono.just(new UnitOfMeasure());

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(testIngredientId);
        ingredientCommand.setRecipeId(testRecipeId);
        ingredientCommand.setDescription(b4IngredientDescription);
        ingredientCommand.setAmount(testIngredientAmount);
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(testUOMId);

        RecipeCommand mockB4Recipe = new RecipeCommand();
        mockB4Recipe.setId(testIngredientId);
        mockB4Recipe.getIngredients().add(ingredientCommand);




        Recipe mockSavedRecipe = new Recipe();
        mockSavedRecipe.setId(testRecipeId);
        Ingredient mockSavedIngredient = new Ingredient();
        mockSavedIngredient.setId(testIngredientId);
        //mockSavedIngredient.setRecipe(mockSavedRecipe);
        mockSavedIngredient.setDescription(testIngredientDescription);
        mockSavedIngredient.setAmount(testIngredientAmount);
        UnitOfMeasure mockSavedUom = new UnitOfMeasure();
        mockSavedUom.setId(testUOMId);
        mockSavedIngredient.setUom(mockSavedUom);
        mockSavedRecipe.getIngredients().add(mockSavedIngredient);
        Mono<Recipe> mockMonoSavedRecipe=Mono.just(mockSavedRecipe);


        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);
        when(mockUomReactiveRepository.findById(anyString())).thenReturn(uomMono);
        when(mockRecipeReactiveRepository.save(any(Recipe.class))).thenReturn(mockMonoSavedRecipe);

        IngredientCommand resultCommand = ingredientService.saveIngredient(ingredientCommand).block();

        assertEquals(testIngredientId, resultCommand.getId());
        assertEquals(testIngredientDescription, resultCommand.getDescription());
        verify(mockRecipeReactiveRepository, times(1)).findById(testRecipeId);
        verify(mockUomReactiveRepository, times(1)).findById(testUOMId);
        verify(mockRecipeReactiveRepository, times(1)).save(any(Recipe.class));

    }

    //@Test
    //void deleteAnIngredient() {
    //    String testRecipeId = "11";
    //    String testIngredientId = "3";
    //
    //    Optional<Recipe> recipeOptional = Optional.of(new Recipe());
    //    Ingredient ingredient = new Ingredient();
    //    ingredient.setId(testIngredientId);
    //
    //    recipeOptional.get().setId(testIngredientId);
    //    recipeOptional.get().getIngredients().add(ingredient);
    //
    //    when(mockRecipeRepository.findById(any())).thenReturn(recipeOptional);
    //
    //    ingredientService.deleteAnIngredient(testRecipeId, testIngredientId);
    //    verify(mockRecipeRepository, times(1)).findById(testRecipeId);
    //    verify(mockRecipeRepository, times(1)).save(recipeOptional.get());
    //
    //
    //}

}