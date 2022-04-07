package guru.springframework.bootstrap;


import guru.springframework.model.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Component
@Profile({"DEV","PROD","dev","prod"})
public class BootStrapDataDEVandPRD implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public BootStrapDataDEVandPRD(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Transactional
    private void initUOMs() {

        ArrayList<String> uomDescriptionList= new ArrayList<String>(Arrays.asList(
                "Teaspoon",
                "Tablespoon",
                "Cup",
                "Pinch",
                "Ounce",
                "Pint",
                "",
                "Dash"));

        if (unitOfMeasureRepository.count()==0) {
            uomDescriptionList.stream().forEach((uomDescription)->{
                UnitOfMeasure uom =new UnitOfMeasure();
                uom.setDescription(uomDescription);
                unitOfMeasureRepository.save(uom);
            });
        }

    }

    @Transactional
    private void initCategory() {
        ArrayList<String> catDescriptionList= new ArrayList<String>(Arrays.asList(
                "American",
                "Italian",
                "Mexican",
                "Fast Food"));

        if (categoryRepository.count()==0) {
            catDescriptionList.stream().forEach((catDescription)->{
                Category category =new Category();
                category.setDescription(catDescription);
                categoryRepository.save(category);
            });
        }

    }

    private void doDBInit() {
        initUOMs();
        initCategory();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Started in Bootstrap");
        doDBInit();

    }
}
