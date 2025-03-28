package com.shop.buy;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buy")
@RequiredArgsConstructor
public class BuyController {
    private final BuyService buyService;

    @PostMapping
    public ResponseEntity<?> buyProduct(@RequestBody @Valid BuyDto buyDto,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }
        try {
            Long buyId = buyService.buyProduct(buyDto, userDetails.getUsername());
            return new ResponseEntity<>(buyId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
