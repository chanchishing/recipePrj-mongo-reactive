package guru.springframework.service;

import guru.springframework.model.Recipe;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageService {

    Mono<Recipe> saveRecipeImage(String recipeId, MultipartFile imageFile);
}
