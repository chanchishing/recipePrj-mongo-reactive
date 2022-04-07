package guru.springframework.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class UnitOfMeasure {
    @Id
    String id;

    private String description;

}
