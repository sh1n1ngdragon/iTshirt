package com.shop.item;

import com.shop.constant.ItemSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
   private Long id;

   @NotBlank(message = "상품명은 필수 입력 값입니다.")
   private String itemNm;

   @NotNull(message = "가격은 필수 입력 값입니다.")
   private Integer price;

   @NotBlank(message = "이름은 필수 입력 값입니다.")
   private String itemDetail;

   @NotNull(message = "재고는 필수 입력 값입니다.")
   private Integer stockNumber;

   private ItemSellStatus itemSellStatus;

   @DateTimeFormat(pattern = "yyyy-MM-dd")
   @NotNull(message = "출발일은 필수 입력 값입니다.")
   private LocalDate checkinDate;
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   @NotNull(message = "도착일은 필수 입력 값입니다.")
   private LocalDate checkoutDate;
   // ------------------------------------------------------------
   //ItemImg
   private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 이미지 정보
   private List<Long> itemImgIds = new ArrayList<>(); //상품 이미지 아이디

   // -------------------------------------------------
   // ModelMapper
   private static ModelMapper modelMapper = new ModelMapper();

   public Item createItem(){
      return modelMapper.map(this, Item.class);
   }
   public static ItemFormDto of(Item item){
      //Item -> ItemFormDto 연결
      return modelMapper.map(item,ItemFormDto.class);
   }
}
