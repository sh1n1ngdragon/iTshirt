package com.shop.product;

import com.shop.constant.ItemSellStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDto {
   private Long id;
   private String subject;
   private int price;
   private String image;
   private String url;
   private String detail;
   private ItemSellStatus sellStatus;
   private int stockNumber;
}
