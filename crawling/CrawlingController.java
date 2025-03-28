package com.shop.crawling;

import com.shop.product.ProductDto;
import com.shop.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlingController {

    private final CrawlingService crawlingService;
    private final ProductService productService; // Product 서비스 추가

    // ✅ 크롤링된 데이터 조회 (Product 테이블 기준)
    @GetMapping("/crawling")
    public String displayCrawlingData(Model model) {
        log.info("크롤링 조회 프로그램을 시작합니다.");
        List<ProductDto> productDtoList = productService.getAllProducts(); // ✅ 변경된 부분
        log.info("저장된 데이터 : {}", productDtoList);
        model.addAttribute("productDtoList", productDtoList);
        return "crawling/start";
    }

    // ✅ GET 요청 차단 (업데이트는 POST 요청만 허용)
    @GetMapping("/crawling/update")
    public ResponseEntity<String> handleGetRequestForUpdate() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("GET 요청은 지원되지 않습니다. POST 요청만 가능합니다.");
    }

    // ✅ 크롤링 데이터 업데이트 (Product 테이블에 저장)
    @PostMapping("/crawling/update")
    public String updateCrawlingData(Model model) {
        log.info("업데이트를 위한 크롤링 프로세스를 실행");
        List<ProductDto> productDtoList = crawlingService.getCrawlingData();  // ✅ 크롤링 실행

        if (productDtoList.isEmpty()) {
            log.error("크롤링된 데이터가 없습니다.");
        } else {
            log.info("크롤링된 데이터 수 : {}", productDtoList.size());
        }

        productService.saveProducts(productDtoList); // ✅ Product 테이블에 저장
        model.addAttribute("productDtoList", productDtoList);
        log.info("크롤링 및 업데이트 완료.");
        return "redirect:/crawling";  // ✅ 크롤링 완료 후 리다이렉트
    }

    // ✅ API로 크롤링 데이터 조회 (페이징 적용)
    @GetMapping("/crawling/api")
    @ResponseBody
    public List<ProductDto> getCrawlingData(@RequestParam int page, @RequestParam int size) {
        return productService.getPagedData(page, size);  // ✅ Product 데이터 페이징 조회
    }
}
