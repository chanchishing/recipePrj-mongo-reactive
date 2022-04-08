package guru.springframework.repositories.reactive;

import guru.springframework.bootstrap.BootStrapDataDefault;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@DataJpaTest
@DataMongoTest
@Slf4j
class UnitOfMeasureReactiveRepositoryIT {

    //@Autowired
    //UnitOfMeasureRepository unitOfMeasureRepository;

    //@Autowired
    //CategoryRepository categoryRepository;

    //@Autowired
    //RecipeRepository recipeRepository;

    //@Autowired
    //RecipeReactiveRepository recipeReactiveRepository;

    //@Autowired
    //CategoryReactiveRepository categoryReactiveRepository;

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;


    @BeforeEach
    void setUp() throws Exception {

        unitOfMeasureReactiveRepository.deleteAll().block();

        //BootStrapDataDefault recipeBootStrap = new BootStrapDataDefault(recipeRepository,categoryRepository,unitOfMeasureRepository,
        //        recipeReactiveRepository,
        //        categoryReactiveRepository,
        //        unitOfMeasureReactiveRepository);

        //recipeBootStrap.run("");
    }

    @Test
    void testAdd() {
        Map<String, UnitOfMeasure> uomS = Map.ofEntries(
                entry("",new UnitOfMeasure()),
                entry("Teaspoon", new UnitOfMeasure()),
                entry("Tablespoon", new UnitOfMeasure()),
                entry("Pinch",new UnitOfMeasure()),
                entry("Cup",new UnitOfMeasure()),
                entry("Ounce",new UnitOfMeasure()),
                entry("Pint",new UnitOfMeasure()),
                entry("Dash",new UnitOfMeasure()));

        Iterator uomIter =uomS.keySet().iterator();
        unitOfMeasureReactiveRepository.insert(uomS.values()).blockLast();

        assertEquals(uomS.size(),unitOfMeasureReactiveRepository.findAll().count().block());

    }
}