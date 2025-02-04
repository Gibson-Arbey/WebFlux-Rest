package co.webflux_rest.service.impl;

import org.springframework.stereotype.Service;

import co.webflux_rest.entity.CategoryEntity;
import co.webflux_rest.repository.CategoryRepository;
import co.webflux_rest.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public Flux<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Mono<CategoryEntity> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Mono<CategoryEntity> save(CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }

    @Override
    public Mono<Void> delete(CategoryEntity categoryEntity) {
        return categoryRepository.delete(categoryEntity);
    }
    
}
