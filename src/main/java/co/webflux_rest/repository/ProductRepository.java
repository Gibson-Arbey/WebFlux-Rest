package co.webflux_rest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import co.webflux_rest.entity.ProductEntity;

public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String>{
    
}