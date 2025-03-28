package com.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

   private final ProductRepository productRepository;

   /**
    * 크롤링 데이터를 DB에 저장
    */
   @Transactional
   public void saveProducts(List<ProductDto> productDtoList) {
      List<Product> products = productDtoList.stream()
              .map(dto -> {
                 Product product = new Product();
                 product.setSubject(dto.getSubject());
                 product.setPrice(dto.getPrice());
                 product.setImageUrl(dto.getImage());
                 product.setProductUrl(dto.getUrl());
                 product.setDetail(dto.getDetail());
                 product.setSellStatus(dto.getSellStatus());
                 product.setStockNumber(dto.getStockNumber());
                 return product;
              })
              .collect(Collectors.toList());

      productRepository.saveAll(products);
      System.out.println("✅ 저장된 상품 개수: " + products.size());
   }

   /**
    * 전체 저장된 상품 목록 조회
    */
   /**
    * 전체 저장된 상품 목록 조회
    */
   @Transactional(readOnly = true)
   public List<ProductDto> getAllProducts() {
      return productRepository.findAll().stream()
              .map(entity -> ProductDto.builder()
                      .subject(entity.getSubject())
                      .price(entity.getPrice())
                      .image(entity.getImageUrl())
                      .url(entity.getProductUrl())
                      .detail(entity.getDetail())
                      .sellStatus(entity.getSellStatus())
                      .stockNumber(entity.getStockNumber())
                      .build())
              .collect(Collectors.toList());
   }


   /**
    * 페이징 처리된 상품 데이터 조회 (무한 스크롤 적용)
    */
   @Transactional(readOnly = true)
   public List<ProductDto> getPagedData(int page, int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<Product> productPage = productRepository.findAll(pageable);

      return productPage.getContent().stream()
              .map(entity -> ProductDto.builder()
                      .subject(entity.getSubject())
                      .price(entity.getPrice())
                      .image(entity.getImageUrl())
                      .url(entity.getProductUrl())
                      .detail(entity.getDetail())
                      .sellStatus(entity.getSellStatus())
                      .stockNumber(entity.getStockNumber())
                      .id(entity.getId())
                      .build())
              .collect(Collectors.toList());
   }
   /**
    * 특정 ID의 상품 상세 조회
    */
   /**
    * 특정 ID의 상품 상세 조회
    */

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + id));

        return ProductDto.builder()
                .id(product.getId())  // ✅ ID 추가
                .subject(product.getSubject())
                .price(product.getPrice())
                .image(product.getImageUrl())
                .url(product.getProductUrl())
                .detail(product.getDetail())
                .sellStatus(product.getSellStatus())
                .stockNumber(product.getStockNumber())
                .build();
    }


}
