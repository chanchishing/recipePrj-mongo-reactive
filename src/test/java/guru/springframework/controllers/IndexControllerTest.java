package guru.springframework.controllers;

import guru.springframework.model.Recipe;
import guru.springframework.service.RecipeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;



class IndexControllerTest {

    IndexController indexController;

    @Mock
    private RecipeServiceImpl recipeService;
    @Mock
    private Model model;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable= MockitoAnnotations.openMocks(this);
        indexController=new IndexController(recipeService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void mvcTest() throws Exception{
        MockMvc mockMvc= MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    void getIndexPage() {

        //given
        List<Recipe> recipes=new ArrayList<>();
        recipes.add(new Recipe());
        recipes.add(new Recipe());

        when(recipeService.getRecipeList()).thenReturn(recipes);

        ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewNameReturned=indexController.getIndexPage(model);

        //then
        // (a) verify "index" is returned from indexController.getIndexPage()
        assertEquals("index",viewNameReturned);

        //then (b) check recipeService.getRecipeList() is called once
        verify(recipeService,times(1)).getRecipeList();

        //then (c) check model.addAttribute("recipes",<List>) is called once and 2 recipes is added
        verify(model,times(1)).addAttribute(eq("recipes"),argumentCaptor.capture());
        List<Recipe> listInController = argumentCaptor.getValue();
        assertEquals(2,listInController.size());

    }
}