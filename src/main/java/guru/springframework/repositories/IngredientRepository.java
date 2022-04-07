package guru.springframework.repositories;

import guru.springframework.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,String> {

}
