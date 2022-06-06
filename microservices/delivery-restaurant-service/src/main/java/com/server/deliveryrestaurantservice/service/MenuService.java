package com.server.deliveryrestaurantservice.service;

import com.server.deliveryrestaurantservice.mapper.MenuMapper;
import com.server.deliveryrestaurantservice.model.dto.response.MenuResponse;
import com.server.deliveryrestaurantservice.model.entity.Menu;
import com.server.deliveryrestaurantservice.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuResponse getMenuByRestaurantId(Long restaurantId) {
        Menu menu = menuRepository.findByRestaurantId(restaurantId)
                .orElseThrow(EntityNotFoundException::new);
        return MenuMapper.mapToDto(menu);
    }
}
