package com.kosnik.routes;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public class AuthenticationRoutes {

    private final static String AUTHENTICATION_SERVICE_NAME = "lb://AUTHENTICATION-SERVICE";
    private final static String AUTHENTICATION_SERVICE_API_URI = "/api/v1/auth/**";
    public static RouteLocatorBuilder.Builder route(RouteLocatorBuilder.Builder routes) {
        return routes
                .route("authentication-service-api",
                        route -> route.path(AUTHENTICATION_SERVICE_API_URI)
                                .uri(AUTHENTICATION_SERVICE_NAME)
                );
    }
}
