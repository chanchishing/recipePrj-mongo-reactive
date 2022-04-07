package guru.springframework.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(exclude = "recipe")
@Document
public class Notes {
    @Id
    private String id;

    private Recipe recipe;

    private String recipeNotes;

}
