package com.shop.buy;

import com.shop.member.Member;
import com.shop.member.MemberRepository;
import com.shop.product.Product;
import com.shop.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BuyService {
    private final BuyRepository buyRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public Long buyProduct(BuyDto buyDto, String email) {
        Product product = productRepository.findById(buyDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        Member member = memberRepository.findByEmail(email);

        Buy buy = Buy.createBuy(
                member,
                product,
                buyDto.getQuantity(),
                buyDto.getTotalPrice(),
                buyDto.getImpUid(),  // ✅ 결제 고유번호 저장
                buyDto.getMerchantUid()  // ✅ 주문번호 저장
        );
        buyRepository.save(buy);

        return buy.getId();
    }
}
