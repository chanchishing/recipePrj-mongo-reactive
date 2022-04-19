package guru.springframework.service;

import guru.springframework.model.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Recipe> saveRecipeImage(String recipeId, MultipartFile imageFile) {

        Recipe recipe;

        //Check recipe is an existing recipe
        try {
            recipe = recipeReactiveRepository.findById(recipeId).block();
            if (recipe.equals(Mono.empty().block())) {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException noElement) {
            log.error("Recipe Not Found recipeId="+String.valueOf(recipeId));
            throw noElement;
        } catch (Exception e){
            throw e;
        }

        try {
            Byte[] byteObjects = new Byte[imageFile.getBytes().length];

            int i = 0;

            for (byte b : imageFile.getBytes()){
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);
        } catch (IOException IOe){
            IOe.printStackTrace();
        }

        return recipeReactiveRepository.save(recipe);

    }
}
