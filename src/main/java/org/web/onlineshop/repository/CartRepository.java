package org.web.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> { }