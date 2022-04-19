package guru.springframework.service;

import guru.springframework.model.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    private AutoCloseable closeable;
    ImageService imageService;

    @Mock
    RecipeReactiveRepository mockRecipeReactiveRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
         imageService= new ImageServiceImpl(mockRecipeReactiveRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void saveRecipeImage() throws IOException {

        String testRecipeId="1";
        MultipartFile mockImageFile=new MockMultipartFile("mockImageFile","test.png","image/png","some bytes".getBytes());

        Recipe recipe=new Recipe();
        recipe.setId(testRecipeId);
        Mono<Recipe> monoRecipe=Mono.just(recipe);


        when(mockRecipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveRecipeImage(testRecipeId,mockImageFile);


        verify(mockRecipeReactiveRepository, times(1)).save(argumentCaptor.capture());
        Recipe recipeToSave = argumentCaptor.getValue();
        assertEquals(mockImageFile.getBytes().length, recipeToSave.getImage().length);
    }

}