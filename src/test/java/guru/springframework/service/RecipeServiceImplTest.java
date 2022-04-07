package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

//import static org.junit.Assert.*;
import java.util.*;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;
    String testId="1";


    @Mock
    RecipeRepository mockRecipeRepository;

    @Mock
    RecipeCommandToRecipe mockCommandToRecipe;

    @Mock
    RecipeToRecipeCommand mockRecipeToCommand;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable=MockitoAnnotations.openMocks(this);
        recipeService= new RecipeServiceImpl(mockRecipeRepository,
                mockRecipeToCommand,
                mockCommandToRecipe);
    }

    @AfterEach
    void tearDown() throws Exception{
        closeable.close();
    }

    @Test
    public void getRecipeList() {
        Recipe dummyRecipe= new Recipe();
        List<Recipe> dummyRecipeList= new ArrayList<>();
        dummyRecipeList.add(dummyRecipe);


        when(mockRecipeRepository.findAll()).thenReturn(dummyRecipeList);

        List<Recipe> recipeList=recipeService.getRecipeList();
        assertEquals(1,recipeList.size());
        verify(mockRecipeRepository,times(1)).findAll();
    }

    @Test
    void getRecipe() {


        Recipe recipe= new Recipe();
        recipe.setId(testId);
        Optional<Recipe> optionalRecipe=Optional.of(recipe);

        when(mockRecipeRepository.findById(anyString())).thenReturn(optionalRecipe);

        Optional<Recipe> resultRecipe= mockRecipeRepository.findById(testId);

        assertEquals(testId,optionalRecipe.get().getId());
        assertNotNull(optionalRecipe);
        verify(mockRecipeRepository,times(1)).findById(testId);


    }

    @Test
    void getRecipeNotFound() {
        Optional<Recipe> emptyRecipeOptional= Optional.empty();

        when(mockRecipeRepository.findById(anyString())).thenReturn(emptyRecipeOptional);

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            recipeService.getRecipe(testId);
        });

        Assertions.assertEquals("Recipe not found for id:"+String.valueOf(testId), thrown.getMessage());



    }

    @Test
    void getRecipeCommandById() {


        Recipe recipe=new Recipe();
        recipe.setId(testId);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        RecipeCommand command= new RecipeCommand();
        command.setId(testId);

        when(mockRecipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));
        when(mockRecipeToCommand.convert(any(Recipe.class))).thenReturn(command);

        RecipeCommand resultCommand=recipeService.getRecipeCommandById(testId);

        assertEquals(testId,resultCommand.getId());
        assertNotNull(resultCommand);
        verify(mockRecipeRepository,times(1)).findById(testId);


    }

    @Test
    void deleteRecipeById(){

        recipeService.deleteRecipeById(testId);

        verify(mockRecipeRepository,times(1)).deleteById(testId);
    }

}