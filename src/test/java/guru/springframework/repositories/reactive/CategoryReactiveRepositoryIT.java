package guru.springframework.repositories.reactive;

import guru.springframework.model.Category;
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
class CategoryReactiveRepositoryIT {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    Map<String, Category> categoryS = Map.ofEntries(
            entry("American",new Category()),
            entry("Italian",new Category()),
            entry("Mexican",new Category()),
            entry("Fast Food",new Category())
    );

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();

        categoryS.forEach((description, catElement) -> {
            catElement.setDescription(description);
        });
    }

    @Test
    void testAdd() {
        categoryReactiveRepository.insert(categoryS.values()).blockLast();
        assertEquals(categoryS.size(),categoryReactiveRepository.findAll().count().block());
    }

    @Test
    void findByDescription() {
        categoryReactiveRepository.insert(categoryS.values()).blockLast();
        Category catAmerican = categoryReactiveRepository.findByDescription("American").block();
        assertEquals("American",catAmerican.getDescription());
        assertNotNull(catAmerican.getId());
    }

}