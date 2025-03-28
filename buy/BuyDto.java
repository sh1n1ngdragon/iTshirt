package com.shop.buy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyDto {

    @NotNull(message = "상품 ID는 필수 입력 값입니다.")
    private Long productId; // 상품 ID

    @NotNull(message = "상품명은 필수 입력 값입니다.")
    private String productName; // 상품명 추가 ✅

    @Min(value = 1, message = "최소 1개 이상 구매해야 합니다.")
    private int quantity; // 구매 수량

    @NotNull(message = "총 가격은 필수 입력 값입니다.")
    private int totalPrice; // 총 결제 금액 추가 ✅

    private String impUid; // 결제 고유번호 ✅
    private String merchantUid; // 가맹점 주문번호 ✅
}
