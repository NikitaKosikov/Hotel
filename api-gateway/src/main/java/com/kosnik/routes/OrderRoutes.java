package com.kosnik.routes;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public class OrderRoutes {

    private final static String ORDER_SERVICE_NAME = "lb://ORDER-SERVICE";
    private final static String ORDER_SERVICE_API_URI = "/api/v1/orders/**";
    public static RouteLocatorBuilder.Builder route(RouteLocatorBuilder.Builder routes) {
        return routes
                .route("order-service-orders-api",
                        route->route.path(ORDER_SERVICE_API_URI)
                                .uri(ORDER_SERVICE_NAME)
                );
    }
}
