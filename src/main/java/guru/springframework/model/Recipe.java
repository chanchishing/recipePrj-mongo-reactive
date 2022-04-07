package guru.springframework.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document
public class Recipe {

    @Id
    private String id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;

    private Set<Ingredient> ingredients = new HashSet<>();

    private Byte[] image;

    private Notes notes;

    @DBRef
    private Set<Category> categories = new HashSet<>();

    public void setNotes(Notes notes) {
        this.notes = notes;
        //notes.setRecipe(this);
    }

    public void addIngredient(Ingredient ingredient) {
        //ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
    }

}
