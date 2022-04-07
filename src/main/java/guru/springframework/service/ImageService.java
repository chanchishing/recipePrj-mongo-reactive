package guru.springframework.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveRecipeImage(String recipeId, MultipartFile imageFile);
}
