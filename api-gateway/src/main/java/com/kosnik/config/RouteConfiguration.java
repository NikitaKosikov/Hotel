package com.kosnik.config;

import com.kosnik.routes.*;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        AuthenticationRoutes.route(routes);
        DiscoveryServerRoutes.route(routes);
        OrderRoutes.route(routes);
        PaymentRoutes.route(routes);
        RoomRoutes.route(routes);
        UserRoutes.route(routes);
        return builder.routes().build();
    }
}
