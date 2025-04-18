package com.shop.cart;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {


   Cart findByMemberId(Long memberId);

}
