package com.kosnik.routes;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public class DiscoveryServerRoutes {

    private final static String EUREKA_SERVER_URL = "http://localhost:8761";
    public static void route(RouteLocatorBuilder.Builder routes) {
        routes
                .route("eureka-server-static",
                        route->route.path("/eureka/**")
                                .uri(EUREKA_SERVER_URL)
                );
    }
}
