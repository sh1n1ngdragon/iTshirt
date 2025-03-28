package com.shop.buy;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findByMemberEmail(String email);
}
