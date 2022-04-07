package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.Recipe;
import guru.springframework.service.IngredientServiceImpl;
import guru.springframework.service.RecipeServiceImpl;
import guru.springframework.service.UnitOfMeasureService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;


import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {


    IngredientController ingredientController;

    MockMvc mockMvc;
    String testIdStr,testIngredientIdStr;



    @Mock
    private RecipeServiceImpl mockRecipeService;

    @Mock
    private IngredientServiceImpl mockIngredientService;

    @Mock
    private UnitOfMeasureService mockUnitOfMeasureService;

    @Mock
    private Model mockModel;
    private AutoCloseable closeable;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ingredientController = new IngredientController(mockRecipeService,mockIngredientService,mockUnitOfMeasureService);

        testIdStr = "1";
        testIngredientIdStr = "2";

        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void listIngredients() throws Exception{
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(testIdStr);

        when(mockRecipeService.getRecipeCommandById(testIdStr)).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/" + testIdStr + "/ingredients/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));


        verify(mockRecipeService, times(1)).getRecipeCommandById(testIdStr);
    }

    @Test
    void ShowIngredient() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(testIngredientIdStr);
        ingredientCommand.setRecipeId(testIdStr);

        when(mockIngredientService.findByRecipeIDAndIngredientId(testIdStr,testIngredientIdStr)).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/" + testIdStr + "/ingredients/" + testIngredientIdStr + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(mockIngredientService, times(1)).findByRecipeIDAndIngredientId(testIdStr,testIngredientIdStr);
    }

    @Test
    void loadIngredientToUpdate() throws Exception {
        String uom1TestId = "9";
        String uom2TestId = "10";

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(testIngredientIdStr);
        ingredientCommand.setRecipeId(testIdStr);

        UnitOfMeasureCommand uomCommand1 = new UnitOfMeasureCommand();
        uomCommand1.setId(uom1TestId);
        UnitOfMeasureCommand uomCommand2 = new UnitOfMeasureCommand();
        uomCommand2.setId(uom2TestId);
        Set<UnitOfMeasureCommand> uomTestList = new HashSet<>();
        uomTestList.add(uomCommand1);
        uomTestList.add(uomCommand2);


        when(mockIngredientService.findByRecipeIDAndIngredientId(testIdStr, testIngredientIdStr)).thenReturn(ingredientCommand);
        when(mockUnitOfMeasureService.getUomList()).thenReturn(uomTestList);


        mockMvc.perform(get("/recipe/" + testIdStr + "/ingredients/" + testIngredientIdStr + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(mockIngredientService, times(1)).findByRecipeIDAndIngredientId(testIdStr, testIngredientIdStr);
        verify(mockUnitOfMeasureService, times(1)).getUomList();

    }

    @Test
    void updateIngredient() throws Exception {

        IngredientCommand mockSavedIngredient=new IngredientCommand();
        mockSavedIngredient.setRecipeId(testIdStr);
        mockSavedIngredient.setId(testIngredientIdStr);

        when(mockIngredientService.saveIngredient(any(IngredientCommand.class))).thenReturn(mockSavedIngredient);

        mockMvc.perform(post("/recipe/" + testIdStr + "/ingredient"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + testIdStr + "/ingredients/" + testIngredientIdStr + "/show"));


    }

    @Test
    void loadIngredientFormToAdd() throws Exception {
        String testRecipeId="1";
        String uom1TestId = "9";
        String uom2TestId = "10";

        UnitOfMeasureCommand uomCommand1 = new UnitOfMeasureCommand();
        uomCommand1.setId(uom1TestId);
        UnitOfMeasureCommand uomCommand2 = new UnitOfMeasureCommand();
        uomCommand2.setId(uom2TestId);
        Set<UnitOfMeasureCommand> uomTestList = new HashSet<>();
        uomTestList.add(uomCommand1);
        uomTestList.add(uomCommand2);

        Recipe mockRecipe=new Recipe();
        mockRecipe.setId(testRecipeId);

        when(mockUnitOfMeasureService.getUomList()).thenReturn(uomTestList);
        when(mockRecipeService.getRecipe(anyString())).thenReturn(mockRecipe);

        mockMvc.perform(get("/recipe/" + testIdStr + "/ingredients/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(model().attribute("ingredient",hasProperty("recipeId",is(testIdStr))))
                .andExpect(model().attribute("uomList",hasSize(uomTestList.size())));


    }

    @Test
    void deleteIngredient() throws Exception {
        String testRecipeId="1";
        String testIngredientId = "9";

        mockMvc.perform(get("/recipe/" + String.valueOf(testRecipeId) + "/ingredients/"+ String.valueOf(testIngredientId)+"/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + String.valueOf(testRecipeId) + "/ingredients/"));

        verify(mockIngredientService,times(1)).deleteAnIngredient(testRecipeId,testIngredientId);
    }
}