package guru.springframework.repositories.reactive;

import guru.springframework.model.UnitOfMeasure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Slf4j
class UnitOfMeasureReactiveRepositoryTest {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;


    Map<String, UnitOfMeasure> uomS = Map.ofEntries(
            entry("",new UnitOfMeasure()),
            entry("Teaspoon", new UnitOfMeasure()),
            entry("Tablespoon", new UnitOfMeasure()),
            entry("Pinch",new UnitOfMeasure()),
            entry("Cup",new UnitOfMeasure()),
            entry("Ounce",new UnitOfMeasure()),
            entry("Pint",new UnitOfMeasure()),
            entry("Dash",new UnitOfMeasure()));

    @BeforeEach
    void setUp() throws Exception {

        unitOfMeasureReactiveRepository.deleteAll().block();

        uomS.forEach((description, uomElement) -> {
                    uomElement.setDescription(description);
                });
    }

    @Test
    void testAdd() {
        unitOfMeasureReactiveRepository.insert(uomS.values()).blockLast();
        assertEquals(uomS.size(),unitOfMeasureReactiveRepository.findAll().count().block());

    }

    @Test
    void findByDescription() {
        unitOfMeasureReactiveRepository.insert(uomS.values()).blockLast();
        UnitOfMeasure uomTeaspoon = unitOfMeasureReactiveRepository.findByDescription("Teaspoon").block();
        assertEquals("Teaspoon",uomTeaspoon.getDescription());
        assertNotNull(uomTeaspoon.getId());
    }
}