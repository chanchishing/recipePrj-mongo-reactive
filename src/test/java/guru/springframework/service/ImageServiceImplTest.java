package guru.springframework.service;

import guru.springframework.controllers.ImageController;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    private AutoCloseable closeable;
    ImageService imageService;

    @Mock
    RecipeRepository mockRecipeRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
         imageService= new ImageServiceImpl(mockRecipeRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void saveRecipeImage() throws IOException {

        String testRecipeId="1";
        MultipartFile mockImageFile=new MockMultipartFile("mockImageFile","test.png","image/png","some bytes".getBytes());

        Optional<Recipe> optionalRecipe=Optional.of(new Recipe());
        optionalRecipe.get().setId(testRecipeId);

        when(mockRecipeRepository.findById(anyString())).thenReturn(optionalRecipe);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveRecipeImage(testRecipeId,mockImageFile);


        verify(mockRecipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(mockImageFile.getBytes().length, savedRecipe.getImage().length);
    }

}