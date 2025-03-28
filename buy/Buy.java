package com.shop.buy;

import com.shop.member.Member;
import com.shop.product.Product;
import com.shop.constant.BuyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "buy")
@Getter
@Setter
@NoArgsConstructor
public class Buy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 구매자 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 구매한 상품 정보

    private int quantity; // 구매 수량
    private int totalPrice; // 총 가격
    private LocalDateTime buyDate; // 구매 날짜

    @Enumerated(EnumType.STRING)
    private BuyStatus buyStatus; // 구매 상태 (결제 대기, 완료, 취소)

    private String impUid; // ✅ 결제 고유번호 추가
    private String merchantUid; // ✅ 가맹점 주문번호 추가

    public static Buy createBuy(Member member, Product product, int quantity, int totalPrice, String impUid, String merchantUid) {
        Buy buy = new Buy();
        buy.setMember(member);
        buy.setProduct(product);
        buy.setQuantity(quantity);
        buy.setTotalPrice(totalPrice);
        buy.setBuyDate(LocalDateTime.now());
        buy.setBuyStatus(BuyStatus.PENDING);
        buy.setImpUid(impUid); // ✅ 결제 정보 저장
        buy.setMerchantUid(merchantUid);
        return buy;
    }

    public void completeBuy() {
        this.buyStatus = BuyStatus.COMPLETED;
    }

    public void cancelBuy() {
        this.buyStatus = BuyStatus.CANCELED;
    }
}
