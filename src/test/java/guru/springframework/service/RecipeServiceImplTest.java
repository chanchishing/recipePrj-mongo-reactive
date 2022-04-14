package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;
    String testId="1";


    @Mock
    RecipeRepository mockRecipeRepository;

    @Mock
    RecipeReactiveRepository mockRecipeReactiveRepository;

    @Mock
    RecipeCommandToRecipe mockCommandToRecipe;

    @Mock
    RecipeToRecipeCommand mockRecipeToCommand;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable=MockitoAnnotations.openMocks(this);
        recipeService= new RecipeServiceImpl(
                mockRecipeReactiveRepository,
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
        Flux<Recipe> dummyRecipeFlux=Flux.just(dummyRecipe);

        when(mockRecipeReactiveRepository.findAll()).thenReturn(dummyRecipeFlux);

        Flux<Recipe> recipeFlux=recipeService.getRecipeList();
        assertEquals(1,recipeFlux.count().block());
        verify(mockRecipeReactiveRepository,times(1)).findAll();
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


    @Disabled
    @Test
    void getRecipeNotFound() {
        Mono<Recipe> emptyRecipeMono= Mono.empty();

        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(emptyRecipeMono);

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            recipeService.getRecipe(testId);
        });

        Assertions.assertEquals("Recipe not found for id:"+String.valueOf(testId), thrown.getMessage());



    }

    @Test
    void getRecipeCommandById() {


        Recipe recipe=new Recipe();
        recipe.setId(testId);
        Mono<Recipe> monoRecipe = Mono.just(recipe);

        RecipeCommand command= new RecipeCommand();
        command.setId(testId);

        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(mockRecipeToCommand.convert(any(Recipe.class))).thenReturn(command);

        RecipeCommand resultCommand=recipeService.getRecipeCommandById(testId).block();

        assertEquals(testId,resultCommand.getId());
        assertNotNull(resultCommand);
        verify(mockRecipeReactiveRepository,times(1)).findById(testId);


    }

    @Test
    void deleteRecipeById(){


        when(mockRecipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        recipeService.deleteRecipeById(testId).block();

        verify(mockRecipeReactiveRepository,times(1)).deleteById(testId);
    }

}