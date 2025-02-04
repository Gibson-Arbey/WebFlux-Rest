package co.webflux_rest.service;

import co.webflux_rest.entity.CategoryEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICategoryService {
    
    Flux<CategoryEntity> findAll();

    Mono<CategoryEntity> findById(String id);

    Mono<CategoryEntity> save(CategoryEntity categoryEntity);

    Mono<Void> delete(CategoryEntity categoryEntity);
}
