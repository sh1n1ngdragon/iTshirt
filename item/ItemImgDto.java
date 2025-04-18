package com.shop.item;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
   private Long id;
   private String imgName;
   private String imgUrl;
   private String oriImgName;
   private String repImgYn;
   private static ModelMapper modelMapper = new ModelMapper();

   public static ItemImgDto of(ItemImg itemImg){
      return modelMapper.map(itemImg, ItemImgDto.class);
   }

}
