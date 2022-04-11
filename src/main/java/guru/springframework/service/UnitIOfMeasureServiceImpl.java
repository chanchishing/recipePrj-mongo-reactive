package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitIOfMeasureServiceImpl implements UnitOfMeasureService{
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand;

    public UnitIOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.unitOfMeasureToCommand = unitOfMeasureToCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> getUomList() {

        //return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasure -> unitOfMeasureToCommand.convert(unitOfMeasure));
        return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasureToCommand::convert);

    }

    //@Override
    //public Set<UnitOfMeasureCommand> getUomList() {
    //    Iterable<UnitOfMeasure> unitOfMeasures=unitOfMeasureRepository.findAll();
    //
    //    return StreamSupport.stream(unitOfMeasures.spliterator(),false)
    //            .map(unitOfMeasureToCommand::convert)
    //            .collect(Collectors.toSet());
    //
    //}

}
