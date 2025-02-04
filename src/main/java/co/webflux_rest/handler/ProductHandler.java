package co.webflux_rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.status(HttpStatus.OK).body(productService.findAll(), ProductEntity.class);
    }
}
