package com.shop.product;


import com.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @Column(nullable = false,length = 2000)
   private String subject; // 상품명

   @Column(nullable = false)
   private int price; // 상품가격

   @Column(nullable = false)
   private String imageUrl; // 상품이미지 URL

   @Column(nullable = false)
   private String productUrl; // 상품 상세페이지 URL

   @Lob
   private String detail; // 상품 상세 설명

   @Enumerated(EnumType.STRING)
   private ItemSellStatus sellStatus; // 판매 상태

   private int stockNumber; // 재고 수량
}
