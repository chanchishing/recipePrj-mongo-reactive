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
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository mockRecipeRepository;

    @Mock
    UnitOfMeasureRepository mockUomRepository;

    IngredientToIngredientCommand IngredientToCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    IngredientCommandToIngredient commandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(mockRecipeRepository, mockUomRepository, IngredientToCommand, commandToIngredient);
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
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(testRecipeId);
        ingredientCommand.setId(testIngredientId);

        when(mockRecipeRepository.findById(any())).thenReturn(recipeOptional);

        IngredientCommand command = ingredientService.findByRecipeIDAndIngredientId(testRecipeId, testIngredientId);
        verify(mockRecipeRepository, times(1)).findById(testRecipeId);
        //assertEquals(testRecipeId, command.getRecipeId());
        assertEquals(testIngredientId, command.getId());


    }

    @Test
    void saveIngredient() {

        String testRecipeId = "11";
        String testIngredientId = "3";
        String testUOMId = "5";
        BigDecimal testIngredientAmount = BigDecimal.valueOf(2.0);
        String testIngredientDescription = "test Ingredient Description";

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(testIngredientId);
        ingredientCommand.setRecipeId(testRecipeId);
        ingredientCommand.setDescription(testIngredientDescription);
        ingredientCommand.setAmount(testIngredientAmount);
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(testUOMId);
        ingredientCommand.setUom(uomCommand);

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        Optional<UnitOfMeasure> uomOptional = Optional.of(new UnitOfMeasure());

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


        when(mockRecipeRepository.findById(any())).thenReturn(recipeOptional);
        when(mockUomRepository.findById(anyString())).thenReturn(uomOptional);
        when(mockRecipeRepository.save(any(Recipe.class))).thenReturn(mockSavedRecipe);

        IngredientCommand resultCommand = ingredientService.saveIngredient(ingredientCommand);

        assertEquals(testIngredientId, resultCommand.getId());
        assertEquals(testIngredientDescription, resultCommand.getDescription());
        verify(mockRecipeRepository, times(1)).findById(testRecipeId);
        verify(mockUomRepository, times(1)).findById(testUOMId);
        verify(mockRecipeRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    void deleteAnIngredient() {
        String testRecipeId = "11";
        String testIngredientId = "3";

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        Ingredient ingredient = new Ingredient();
        ingredient.setId(testIngredientId);

        recipeOptional.get().setId(testIngredientId);
        recipeOptional.get().getIngredients().add(ingredient);

        when(mockRecipeRepository.findById(any())).thenReturn(recipeOptional);

        ingredientService.deleteAnIngredient(testRecipeId, testIngredientId);
        verify(mockRecipeRepository, times(1)).findById(testRecipeId);
        verify(mockRecipeRepository, times(1)).save(recipeOptional.get());


    }

}