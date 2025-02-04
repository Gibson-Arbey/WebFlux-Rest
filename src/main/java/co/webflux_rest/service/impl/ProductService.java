package co.webflux_rest.service.impl;

import org.springframework.stereotype.Service;

import co.webflux_rest.entity.ProductEntity;
import co.webflux_rest.repository.CategoryRepository;
import co.webflux_rest.repository.ProductRepository;
import co.webflux_rest.service.IProductService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Flux<ProductEntity> findAll() {
        return productRepository.findAll()
                .flatMap(p -> categoryRepository.findById(p.getCategoryId())
                        .map(category -> {
                            p.setCategory(category);
                            return p;
                        }));
    }

    @Override
    public Mono<ProductEntity> findById(String id) {
        return productRepository.findById(id)
                .flatMap(p -> categoryRepository.findById(p.getCategoryId())
                        .map(category -> {
                            p.setCategory(category);
                            return p;
                        }));
    }

    @Override
    public Mono<ProductEntity> save(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public Mono<Void> delete(ProductEntity productEntity) {
        return productRepository.delete(productEntity);
    }

}
