package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
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

    IngredientToIngredientCommand ingredientToCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    IngredientCommandToIngredient commandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(mockRecipeReactiveRepository, mockUomReactiveRepository, ingredientToCommand, commandToIngredient);
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


        UnitOfMeasure mockUom = new UnitOfMeasure();
        mockUom.setId(testUOMId);
        Mono<UnitOfMeasure> mockUomMono= Mono.just(mockUom);

        Recipe mockB4Recipe = new Recipe();
        mockB4Recipe.setId(testRecipeId);
        Ingredient mockB4Ingredient = new Ingredient();
        mockB4Ingredient.setId(testIngredientId);
        mockB4Ingredient.setDescription(b4IngredientDescription);
        mockB4Ingredient.setAmount(testIngredientAmount);
        mockB4Ingredient.setUom(mockUom);
        mockB4Recipe.getIngredients().add(mockB4Ingredient);
        Mono<Recipe> mockB4RecipeMono = Mono.just(mockB4Recipe);
        IngredientCommand ingredientCommand=ingredientToCommand.convert(mockB4Ingredient);
        ingredientCommand.setRecipeId(testRecipeId);

        Recipe mockSavedRecipe = new Recipe();
        mockSavedRecipe.setId(testRecipeId);
        Ingredient mockSavedIngredient = new Ingredient();
        mockSavedIngredient.setId(testIngredientId);
        mockSavedIngredient.setDescription(testIngredientDescription);
        mockSavedIngredient.setAmount(testIngredientAmount);
        mockSavedIngredient.setUom(mockUom);
        mockSavedRecipe.getIngredients().add(mockSavedIngredient);
        Mono<Recipe> mockSavedRecipeMono = Mono.just(mockSavedRecipe);




        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(mockB4RecipeMono);
        when(mockUomReactiveRepository.findById(anyString())).thenReturn(mockUomMono);
        when(mockRecipeReactiveRepository.save(any(Recipe.class))).thenReturn(mockSavedRecipeMono);

        IngredientCommand resultCommand = ingredientService.saveIngredient(ingredientCommand).block();

        assertEquals(testIngredientId, resultCommand.getId());
        assertEquals(testIngredientDescription, resultCommand.getDescription());
        assertEquals(testRecipeId,resultCommand.getRecipeId());
        verify(mockRecipeReactiveRepository, times(1)).findById(testRecipeId);
        verify(mockUomReactiveRepository, times(1)).findById(testUOMId);
        verify(mockRecipeReactiveRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    void deleteAnIngredient() {
        String testRecipeId = "11";
        String testIngredientId = "3";

        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(testIngredientId);

        recipe.setId(testRecipeId);
        recipe.getIngredients().add(ingredient);
        Mono<Recipe> monoRecipe=Mono.just(recipe);


        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(testRecipeId);
        Mono<Recipe> monoSavedRecipe=Mono.just(savedRecipe);


        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(mockRecipeReactiveRepository.save(any())).thenReturn(monoSavedRecipe);

        ingredientService.deleteAnIngredient(testRecipeId, testIngredientId);
        verify(mockRecipeReactiveRepository, times(1)).findById(testRecipeId);
        verify(mockRecipeReactiveRepository, times(1)).save(recipe);


    }

}