package co.webflux_rest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import co.webflux_rest.entity.CategoryEntity;

public interface CategoryRepository extends ReactiveMongoRepository<CategoryEntity, String> {
    
}

