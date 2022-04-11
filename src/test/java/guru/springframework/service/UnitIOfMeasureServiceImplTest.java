package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UnitIOfMeasureServiceImplTest {
    UnitIOfMeasureServiceImpl unitIOfMeasureService;
    private AutoCloseable closeable;
    @Mock
    private UnitOfMeasureReactiveRepository mockUnitOfMeasureReactiveRepository;
    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand=new UnitOfMeasureToUnitOfMeasureCommand();

    @BeforeEach
    void setUp() {
        closeable= MockitoAnnotations.openMocks(this);
        unitIOfMeasureService= new UnitIOfMeasureServiceImpl(mockUnitOfMeasureReactiveRepository,unitOfMeasureToCommand);
    }

    @AfterEach
    void tearDown() throws Exception{
        closeable.close();
    }

    @Test
    void getUomList(){
        UnitOfMeasure uom1,uom2;
        uom1=new UnitOfMeasure();
        uom1.setId("1");
        uom2=new UnitOfMeasure();
        uom2.setId("2");

        Flux<UnitOfMeasure> testUOMFlux= Flux.just(uom1,uom2);

        when(mockUnitOfMeasureReactiveRepository.findAll()).thenReturn(testUOMFlux);

        List<UnitOfMeasureCommand> unitOfMeasures=unitIOfMeasureService.getUomList().collectList().block();

        assertEquals(2,unitOfMeasures.size());
        assertEquals(1,unitOfMeasures.stream().filter(uom->uom.getId().equals("2")).count());
        assertEquals(1,unitOfMeasures.stream().filter(uom->uom.getId().equals("1")).count());
    }
}