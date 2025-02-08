package co.webflux_rest.handler;

import java.time.LocalDate;
import java.util.UUID;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.webflux_rest.entity.ProductEntity;
import co.webflux_rest.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;

    @Value("${upload.path.product}")
    private String pathProduct;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.status(HttpStatus.OK).body(productService.findAll(), ProductEntity.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return productService.findById(id)
                .flatMap(p -> ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(p)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<ProductEntity> productEntity = serverRequest.bodyToMono(ProductEntity.class);
        return productEntity.flatMap(p -> {
            if (p.getCreateAt() == null)
                p.setCreateAt(LocalDate.now());
            return productService.save(p);
        }).flatMap(p -> ServerResponse.status(HttpStatus.CREATED).body(BodyInserters.fromValue(p)));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ProductEntity> productEntity = serverRequest.bodyToMono(ProductEntity.class);
        Mono<ProductEntity> productDb = productService.findById(id);

        return productDb.zipWith(productEntity, (db, req) -> {
            db.setCategoryId(req.getCategoryId());
            db.setPrice(req.getPrice());
            db.setName(req.getName());
            return db;
        }).flatMap(productService::save)
                .flatMap(p -> ServerResponse.status(HttpStatus.OK)
                        .body(BodyInserters.fromValue(p)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return productService.findById(id).flatMap(p -> {
            productService.delete(p);
            return ServerResponse.status(HttpStatus.OK).build();
        }).switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> uploadPhoto(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return serverRequest.multipartData()
                .map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> productService.findById(id)
                        .flatMap(p -> {
                            String filename = UUID.randomUUID().toString() + "-"
                                    + file.filename().replaceAll("[\\\\/:*?\"<>|]", "");
                            p.setPhoto(filename);
                            return file.transferTo(new File(pathProduct + filename))
                                    .then(productService.save(p));
                        }))
                .flatMap(p -> ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(p)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> createProductWithPhoto(ServerRequest serverRequest) {

    Mono<ProductEntity> productEntityMono = serverRequest.multipartData()
            .flatMap(multipart -> {
                FormFieldPart name = (FormFieldPart) multipart.toSingleValueMap().get("name");
                FormFieldPart price = (FormFieldPart) multipart.toSingleValueMap().get("price");
                FormFieldPart categoryId = (FormFieldPart) multipart.toSingleValueMap().get("categoryId");

                ProductEntity product = new ProductEntity(name.value(), Double.parseDouble(price.value()));
                product.setCategoryId(categoryId.value());
                return Mono.just(product);
            });

    return serverRequest.multipartData()
            .flatMap(multipart -> {
                FilePart filePart = (FilePart) multipart.toSingleValueMap().get("file");
                if (filePart == null) {
                    return Mono.empty();
                }
                return Mono.just(filePart);
            })
            .flatMap(filePart -> productEntityMono
                    .flatMap(product -> {
                        String filename = UUID.randomUUID().toString() + "-" + filePart.filename().replaceAll("[\\\\/:*?\"<>|]", "");
                        product.setPhoto(filename);

                        return filePart.transferTo(new File(pathProduct, filename))
                                .then(productService.save(product));
                    }))
            .flatMap(product -> ServerResponse.status(HttpStatus.CREATED).body(BodyInserters.fromValue(product)))
            .switchIfEmpty(ServerResponse.status(HttpStatus.BAD_REQUEST).build()); 
}


}
