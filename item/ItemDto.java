package com.shop.item;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter

public class ItemDto {
   private Long id;
   private String itemNm;
   private Integer price;
   private String itemDetail;
   private String sellStatCd;
   private LocalDateTime regTime;
   private LocalDateTime updateTime;
   private LocalDate checkinDate;
   private LocalDate checkoutDate;
}
