package com.shop.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

   private final ProductService productService;

   /**
    * 전체 상품 목록 페이지
    */
   @GetMapping("/product")
   public String showProductList(Model model) {
      return "product/productList"; // productList.html 렌더링
   }

   /**
    * 무한 스크롤을 위한 페이징 API
    */
   @GetMapping("/products/api")
   @ResponseBody
   public ResponseEntity<List<ProductDto>> getPagedProducts(@RequestParam int page, @RequestParam int size) {
      List<ProductDto> products = productService.getPagedData(page, size);
      return ResponseEntity.ok(products);
   }

   /**
    * 개별 상품 상세 페이지
    */
   @GetMapping("/product/{id}")
   public String showProductDetail(@PathVariable Long id, Model model) {
      log.info("=== 컨트롤러: 상품 상세 요청 ===");
      log.info("요청받은 상품 ID: {}", id);

      ProductDto product = productService.getProductById(id);

      // ✅ product가 null인지 확인
      if (product == null) {
         log.error("상품 데이터를 찾을 수 없습니다! ID: {}", id);
         return "error/404";  // ❌ 상품이 없을 경우 404 페이지로 이동
      }

      log.info("조회된 상품 ID: {}", product.getId());
      log.info("조회된 상품명: {}", product.getSubject());

      model.addAttribute("product", product);
      return "product/productDetail";
   }




}
