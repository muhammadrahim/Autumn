package com.morahim.spring.data;

import com.morahim.spring.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order save(Order order);

    List<Order> findByPostCode(String postCode);

    List<Order> findOrdersByPostCodeAndPlacedAtBetween(String postCode, Date startDate, Date endDate);

}
