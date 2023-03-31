package com.kosnik.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public class PaymentRoutes {

    private final static String PAYMENT_SERVICE_NAME = "lb://PAYMENT-SERVICE";
    private final static String PAYMENT_SERVICE_API_URI = "/api/v1/payments/**";
    public static RouteLocatorBuilder.Builder route(RouteLocatorBuilder.Builder routes) {
        return
                routes
                        .route("payment-service-payments-api",
                                route->route.path(PAYMENT_SERVICE_API_URI)
                                        .uri(PAYMENT_SERVICE_NAME)
                        );
    }
}
