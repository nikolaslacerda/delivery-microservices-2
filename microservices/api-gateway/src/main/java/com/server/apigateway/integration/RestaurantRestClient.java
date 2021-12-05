package com.server.apigateway.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient("monolith")
public interface RestaurantRestClient {

    @GetMapping("/restaurants/{id}")
    Map<String, Object> byId(@PathVariable("id") Long id);
}
