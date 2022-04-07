package guru.springframework.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {

    Category category;

    @BeforeEach
    public void setUp(){
        category =new Category();
    }

    @Test
    public void getId() {
        String idValue="4";
       //System.out.println("abc");
        category.setId(idValue);
        assertEquals(idValue,category.getId());
    }

    @Test
    public void getDescription() {
    }

    @Test
    public void getRecipes() {
    }
}