package com.shop.payment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class PaymentController {

    // 🔹 결제 페이지로 이동하는 컨트롤러
    @GetMapping("/payment")
    public String paymentPage(
            @RequestParam(required = false) Long productId,  // ✅ `required = false` 추가 (디버깅용)
            @RequestParam(required = false) String productName,  // ✅ 상품명 추가
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer totalPrice,
            Model model) {

        // ✅ 디버깅 로그 추가
        System.out.println("📌 Received productId: " + productId);
        System.out.println("📌 Received productName (Encoded): " + productName);
        System.out.println("📌 Received count: " + count);
        System.out.println("📌 Received totalPrice: " + totalPrice);

        // ✅ 값이 제대로 들어왔는지 확인 (NULL 방지)
        if (productId == null || count == null || totalPrice == null || productName == null) {
            System.out.println("❌ Error: 하나 이상의 필드가 NULL입니다.");
            model.addAttribute("error", "상품 정보가 올바르지 않습니다.");
            return "error/paymentError"; // 🚨 에러 페이지로 이동
        }

        // ✅ 상품명 디코딩 (URI 인코딩된 값 복원)
        String decodedProductName = URLDecoder.decode(productName, StandardCharsets.UTF_8);
        System.out.println("📌 Decoded productName: " + decodedProductName);

        // ✅ 결제 페이지에 데이터 추가
        model.addAttribute("productId", productId);
        model.addAttribute("productName", decodedProductName);
        model.addAttribute("count", count);
        model.addAttribute("totalPrice", totalPrice);

        return "payment/payment"; // 🔥 `payment.html`로 이동
    }
}
