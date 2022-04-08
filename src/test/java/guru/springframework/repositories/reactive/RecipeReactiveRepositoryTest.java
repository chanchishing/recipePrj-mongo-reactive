package guru.springframework.repositories.reactive;

import guru.springframework.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Slf4j
class RecipeReactiveRepositoryTest {

    private static final String SOME_DESCRIPTION = "Some Description";

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    void testAdd() {
        Recipe someRecipe=new Recipe();

        someRecipe.setDescription(SOME_DESCRIPTION);

        recipeReactiveRepository.insert(someRecipe).block();
        Recipe recipeFound=recipeReactiveRepository.findAll().blockFirst();
        assertEquals(1,recipeReactiveRepository.count().block());
        assertEquals(SOME_DESCRIPTION,recipeFound.getDescription());
        assertNotNull(recipeFound.getId());
    }
}