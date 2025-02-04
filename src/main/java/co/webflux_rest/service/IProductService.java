package co.webflux_rest.service;


import co.webflux_rest.entity.ProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
    
    Flux<ProductEntity> findAll();

    Mono<ProductEntity> findById(String id);

    Mono<ProductEntity> save(ProductEntity productEntity);

    Mono<Void> delete(ProductEntity productEntity);
}
