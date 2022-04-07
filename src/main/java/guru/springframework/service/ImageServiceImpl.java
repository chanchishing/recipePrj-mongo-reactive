package guru.springframework.service;

import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static java.awt.SystemColor.info;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public void saveRecipeImage(String recipeId, MultipartFile imageFile) {

        Recipe recipe;

        //Check recipe is an existing recipe
        try {
            recipe = recipeRepository.findById(recipeId).orElseThrow();
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

        recipeRepository.save(recipe);

    }
}
