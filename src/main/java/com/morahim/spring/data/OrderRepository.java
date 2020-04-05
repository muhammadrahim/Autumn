package com.morahim.spring.data;

import com.morahim.spring.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
    Order save(Order order);

    List<Order> findByPostCode(String postCode);

    List<Order> findOrdersByPostCodeAndPlacedAtBetween(String postCode, Date startDate, Date endDate);

}
