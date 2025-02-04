package co.webflux_rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.webflux_rest.handler.ProductHandler;
import lombok.RequiredArgsConstructor;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@RequiredArgsConstructor
public class RouterFunctionConfig {

    private final ProductHandler productHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        return route(GET("/api/v2/product").or(GET("/api/v3/product")),
                productHandler::findAll);
    }
}
