package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UnitIOfMeasureServiceImplTest {
    UnitIOfMeasureServiceImpl unitIOfMeasureService;
    private AutoCloseable closeable;
    @Mock
    private UnitOfMeasureRepository mockUnitOfMeasureRepository;
    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand=new UnitOfMeasureToUnitOfMeasureCommand();

    @BeforeEach
    void setUp() {
        closeable= MockitoAnnotations.openMocks(this);
        unitIOfMeasureService= new UnitIOfMeasureServiceImpl(mockUnitOfMeasureRepository,unitOfMeasureToCommand);
    }

    @AfterEach
    void tearDown() throws Exception{
        closeable.close();
    }

    @Test
    void getUomList(){
        ArrayList<UnitOfMeasure> unitOfMeasureIterable= new ArrayList<UnitOfMeasure>();
        UnitOfMeasure uom1,uom2;
        uom1=new UnitOfMeasure();
        uom1.setId("1");
        uom2=new UnitOfMeasure();
        uom2.setId("2");
        unitOfMeasureIterable.add(uom1);
        unitOfMeasureIterable.add(uom2);



        when(mockUnitOfMeasureRepository.findAll()).thenReturn(unitOfMeasureIterable);

        Set<UnitOfMeasureCommand> unitOfMeasures=unitIOfMeasureService.getUomList();

        assertEquals(2,unitOfMeasures.stream().count());
        assertEquals(1,unitOfMeasures.stream().filter(uom->uom.getId().equals("2")).count());
        assertEquals(1,unitOfMeasures.stream().filter(uom->uom.getId().equals("1")).count());
    }
}