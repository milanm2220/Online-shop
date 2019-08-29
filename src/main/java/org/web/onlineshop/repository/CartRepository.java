package org.web.onlineshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.util.OrderStatus;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>
{
	List<Cart> findByState(OrderStatus orderStatus);
}