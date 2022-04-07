package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitIOfMeasureServiceImpl implements UnitOfMeasureService{
    UnitOfMeasureRepository unitOfMeasureRepository;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand;

    public UnitIOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToCommand = unitOfMeasureToCommand;
    }

    @Override
    public Set<UnitOfMeasureCommand> getUomList() {
        Iterable<UnitOfMeasure> unitOfMeasures=unitOfMeasureRepository.findAll();

        return StreamSupport.stream(unitOfMeasures.spliterator(),false)
                .map(unitOfMeasureToCommand::convert)
                .collect(Collectors.toSet());

    }
}
