package com.kosnik.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import java.util.function.Function;

public class UserRoutes {
    private final static String USER_SERVICE_NAME = "lb://USER-SERVICE";
    private final static String USER_SERVICE_API_URI = "/api/v1/users/**";

    public static RouteLocatorBuilder.Builder route(RouteLocatorBuilder.Builder routes){
        return
                routes
                        .route("user-service-users-api",
                                route->route.path(USER_SERVICE_API_URI)
                                        .uri(USER_SERVICE_NAME)
                        );
    }
}
