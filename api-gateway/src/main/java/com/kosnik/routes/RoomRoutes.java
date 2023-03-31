package com.kosnik.routes;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public class RoomRoutes {

    private final static String ROOM_SERVICE_NAME = "lb://ROOM-SERVICE";
    private final static String ROOM_SERVICE_API_URI = "/api/v1/rooms/**";
    public static  RouteLocatorBuilder.Builder route(RouteLocatorBuilder.Builder routes) {
        return
                routes
                        .route("room-service-rooms-api",
                                route->route.path(ROOM_SERVICE_API_URI)
                                        .uri(ROOM_SERVICE_NAME)
                        );
    }
}
