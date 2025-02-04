package co.webflux_rest.controller;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import co.webflux_rest.entity.ProductEntity;
import co.webflux_rest.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @Value("${upload.path.product}")
    private String pathProduct;

    @GetMapping
    public Mono<ResponseEntity<Flux<ProductEntity>>> getAllProducts() {
        return Mono.just(ResponseEntity.ok(productService.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductEntity>> getProductById(@PathVariable String id) {
        return productService.findById(id).map(p -> ResponseEntity.ok().body(p))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public Mono<ResponseEntity<ProductEntity>> createProduct(@Valid @RequestBody ProductEntity productEntity) {
        if (productEntity.getCreateAt() == null)
            productEntity.setCreateAt(LocalDate.now());
        return productService.save(productEntity)
                .map(p -> ResponseEntity.created(URI.create("/api/product/" + p.getCategoryId())).body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductEntity>> updateProduct(@PathVariable String id,
            @RequestBody ProductEntity productEntity) {
        return productService.findById(id).flatMap(p -> {
            p.setCategoryId(productEntity.getCategoryId());
            p.setName(productEntity.getName());
            p.setPrice(productEntity.getPrice());
            return productService.save(p);
        }).map(p -> ResponseEntity.created(URI.create("/api/product/" + p.getCategoryId())).body(p))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {
        return productService.findById(id)
                .flatMap(product -> productService.delete(product)
                        .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/upload/{id}")
    public Mono<ResponseEntity<ProductEntity>> uploadImageProduct(@PathVariable String id, @RequestPart FilePart file) {
        return productService.findById(id)
                .flatMap(product -> {
                    String photoName = UUID.randomUUID() + "-" + file.filename().replaceAll("[\\\\ :]", "");
                    product.setPhoto(photoName);

                    return file.transferTo(new File(pathProduct + photoName))
                            .then(productService.save(product));
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
