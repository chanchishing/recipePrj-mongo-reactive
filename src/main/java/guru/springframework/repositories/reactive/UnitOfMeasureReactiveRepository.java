package guru.springframework.repositories.reactive;

import guru.springframework.model.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure,String> {
}
