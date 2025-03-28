package com.shop.item;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.item.QItem;
import com.shop.item.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

   private JPAQueryFactory queryFactory;

   public ItemRepositoryCustomImpl(EntityManager em){
      this.queryFactory = new JPAQueryFactory(em);
   }
   private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
      return searchSellStatus == null?
              null : QItem.item.itemSellStatus.eq(searchSellStatus);
   }

   private BooleanExpression regDtsAfter(String searchDateType){
      LocalDateTime dateTime = LocalDateTime.now();

      if(StringUtils.equals("all",searchDateType) || searchDateType == null){
         return null;
      }else if(StringUtils.equals("id",searchDateType)){
         dateTime = dateTime.minusDays(1);
      }else if(StringUtils.equals("1w",searchDateType)){
         dateTime = dateTime.minusWeeks(1);
      }else if(StringUtils.equals("1m",searchDateType)){
         dateTime = dateTime.minusMonths(1);
      }else if(StringUtils.equals("6m",searchDateType)){
         dateTime = dateTime.minusMonths(1);
      }
      return QItem.item.regTime.after(dateTime);
   }

   private BooleanExpression searchByLike(String searchBy, String searchQuery){
      if(StringUtils.equals("itemNm",searchBy)){ // 상품명
         return QItem.item.itemNm.like("%"+searchQuery+"%");
      }else if(StringUtils.equals("createdBy",searchBy)){ // 작성자
         return QItem.item.createdBy.like("%"+searchQuery+"%");
      }
      return null;
   }

   @Override
   public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
      // select * from item
      // where
      // 1. null 조건제외
      // 2. null 조건 제외
      // 3. null 조건 제외
      // 정렬을 item id 기준 내림차순
      // offset 0 limit 5
      // 실행
      QueryResults<Item> results = queryFactory.selectFrom(QItem.item).
              where(regDtsAfter(itemSearchDto.getSearchDateType()),
                      searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                      searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
              .orderBy(QItem.item.id.desc())
              .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
      List<Item> content = results.getResults(); // 결과값 -> List로 받음
      long total = results.getTotal();  // 결과과 나온 길이  5 이하
      return new PageImpl<>(content,pageable,total);  // List (=content) , 페이지세팅 (= pageable), 길이  (= total)
   }

   private BooleanExpression itemNmLike(String searchQuery){
      return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
   }

   @Override
   public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
      QItem item = QItem.item;
      QItemImg itemImg = QItemImg.itemImg;
      // select i.id,id.itemNm, i.itemDetail,im.itemImg, i.price from item o, itemimg im join i.id = im.itemid
      // where im.repImgYn = "Y" and i.itemNm like %searchQuery% order by i.id desc
      // QMainItemDto @QueryProjection 을 허용하면 DTO 로 바로 조회 가능
      QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm,
                      item.itemDetail, itemImg.imgUrl,item.price))
              // join 내부조인, repImgYn.eq("Y") 대표이미지만 가져옴.
              .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))
              .where(itemNmLike(itemSearchDto.getSearchQuery()))
              .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
      List<MainItemDto> content = results.getResults();
      long total = results.getTotal();
      return new PageImpl<>(content,pageable,total);
   }
}
