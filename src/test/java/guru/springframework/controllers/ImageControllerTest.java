package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.service.ImageService;
import guru.springframework.service.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    MockMvc mockMvc;
    private AutoCloseable closeable;

    @Mock
    RecipeService mockRecipeService;

    @Mock
    ImageService mockImageService;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ImageController imageController = new ImageController(mockRecipeService,mockImageService);

        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void loadImageUploadPage() throws Exception {
        String testRecipeId="1";
        String testRecipeIdString=String.valueOf(testRecipeId);
        RecipeCommand recipeCommand= new RecipeCommand();
        recipeCommand.setId(testRecipeId);

        when(mockRecipeService.getRecipeCommandById(anyString())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/"+testRecipeIdString+"/image"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/imageUploadForm"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe",is(instanceOf(RecipeCommand.class))))
                .andExpect(model().attribute("recipe",hasProperty("id",is(testRecipeId))));

        verify(mockRecipeService, times(1)).getRecipeCommandById(testRecipeId);
    }

    @Test
    public void getRecipeRecipeNotFound() throws Exception {

        when(mockRecipeService.getRecipeCommandById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception",is(instanceOf(NotFoundException.class))));
    }

    //Disable test as Id is String for MongoDB
    @Disabled
    @Test
    public void getRecipeRecipeIdNotNumeric() throws Exception {

        mockMvc.perform(get("/recipe/txtId/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(model().attribute("exception",is(instanceOf(NumberFormatException.class))));
    }

    @Test
    void saveImageUploaded() throws Exception {
        String testRecipeId="1";

        MockMultipartFile mockImageFile= new MockMultipartFile("imagefile","testing.png",
                "image/png","Just Some Bytes".getBytes());

        mockMvc.perform(multipart("/recipe/"+String.valueOf(testRecipeId)+"/image").file(mockImageFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","/recipe/"+String.valueOf(testRecipeId)+"/show"))
                .andExpect(view().name("redirect:/recipe/"+String.valueOf(testRecipeId)+"/show"));

        verify(mockImageService, times(1)).saveRecipeImage(testRecipeId, mockImageFile);
    }



    @Test
    public void renderImageFromDB() throws Exception {

        //given
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;

        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        command.setImage(bytesBoxed);

        when(mockRecipeService.getRecipeCommandById(anyString())).thenReturn(command);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(s.getBytes().length, responseBytes.length);
    }
}