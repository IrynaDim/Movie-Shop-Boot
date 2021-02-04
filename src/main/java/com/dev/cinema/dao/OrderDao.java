package com.dev.cinema.dao;

import com.dev.cinema.model.Order;
import com.dev.cinema.model.User;
import java.util.List;

public interface OrderDao {
    public Order create(Order order);

    public List<Order> getOrderHistory(User user);
}
