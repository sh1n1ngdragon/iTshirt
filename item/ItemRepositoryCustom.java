package com.shop.item;

import com.shop.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
   Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

   Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
