package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.service.RecipeServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;


import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasProperty.hasProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {

    RecipeController recipeController;

    @Mock
    private RecipeServiceImpl mockRecipeService;
    @Mock
    private Model mockModel;
    private AutoCloseable closeable;

    MockMvc mockMvc;
    String testIdStr;
    Long testIdLong;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(mockRecipeService);

        testIdStr = "1";
        testIdLong = Long.valueOf(testIdStr);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getRecipePage() {

        Recipe recipe = new Recipe();
        recipe.setId(testIdStr);

        when(mockRecipeService.getRecipe(testIdStr)).thenReturn(recipe);
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        String viewNameReturned = recipeController.getRecipePage(testIdStr, mockModel);

        //then
        assertEquals("/recipe/show", viewNameReturned);
        verify(mockRecipeService, times(1)).getRecipe(testIdStr);
        verify(mockModel, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());
        Recipe returnedRecipe = argumentCaptor.getValue();
        assertEquals(testIdStr, returnedRecipe.getId());

    }

    @Test
    public void getRecipePageRecipeNotFound() throws Exception {

        when(mockRecipeService.getRecipe(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception",is(instanceOf(NotFoundException.class))));
    }

    //Disable test as MongoDB id is String
    @Disabled
    @Test
    public void getRecipeRecipeIdNotNumeric() throws Exception {

        //when(mockRecipeService.getRecipe(any())).thenThrow(NumericException.class);

        mockMvc.perform(get("/recipe/txtId/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception",is(instanceOf(NumberFormatException.class))));
    }

    @Test
    void mvcTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(testIdStr);

        when(mockRecipeService.getRecipe(testIdStr)).thenReturn(recipe);

        mockMvc.perform(get("/recipe/" + testIdStr + "/show/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testNewRecipeFormLoad() throws Exception {

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", instanceOf(RecipeCommand.class)))
                .andExpect(view().name("recipe/recipeform"));

    }

    @Test
    void testPostNewRecipeForm() throws Exception {


        RecipeCommand outputCommand = new RecipeCommand();
        outputCommand.setId(testIdStr);

        when(mockRecipeService.saveRecipe(any())).thenReturn(outputCommand);

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id",testIdStr)
                        .param("description","new description")
                        .param("directions","some directions")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + testIdStr + "/show/"));


    }

    @Test
    void testPostNewRecipeFormValidationFailed() throws Exception {


        RecipeCommand outputCommand = new RecipeCommand();
        outputCommand.setId(testIdStr);

        when(mockRecipeService.saveRecipe(any())).thenReturn(outputCommand);
        when(mockRecipeService.getRecipeCommandById(any())).thenReturn(outputCommand);

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id",testIdStr)
                        .param("prepTime",String.valueOf(10000))
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));


    }

    @Test
    void testUpdateContextPath() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(testIdStr);

        when(mockRecipeService.getRecipeCommandById(testIdStr)).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/" + testIdStr + "/update/"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    void testDeleteContextPath() throws Exception {

        mockMvc.perform(get("/recipe/" + testIdStr + "/delete/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
                //.andExpect(model().attributeExists("recipe"));

    }


}