package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> getUomList();
}
