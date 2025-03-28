package com.shop.item;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.BaseEntity;
import com.shop.exception.OutOfStockException;
import com.shop.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

   @Id
   @Column(name="item_id")
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id; //상품코드

   @Column(nullable = false,length =1500)
   private String itemNm; // 상품명

   @Column(name ="price", nullable = false)
   private int price; // 가격

   @Column(nullable = false)
   private int stockNumber;// 수량

   @Lob
   @Column(nullable = false,length = 12000)
   private String itemDetail; // 상품상세설명

   @Enumerated(EnumType.STRING)
   private ItemSellStatus itemSellStatus; // 상품판매 상태

   // private LocalDateTime regTime; // 등록 시간
   // private LocalDateTime updateTime; // 수정 시간
   @Column(name = "checkin_date", nullable = false)
   private LocalDate checkinDate; // 입실일

   @Column(name = "checkout_date", nullable = false)
   private LocalDate checkoutDate; // 퇴실일


   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
           name="member_item",
           joinColumns = @JoinColumn(name = "member_id"),
           inverseJoinColumns = @JoinColumn(name = "item_id")
   )
   private List<Member> member;

   public void updateItem(ItemFormDto itemFormDto){
      this.itemNm=itemFormDto.getItemNm();
      this.price = itemFormDto.getPrice();
      this.stockNumber= itemFormDto.getStockNumber();
      this.itemDetail = itemFormDto.getItemDetail();
      this.itemSellStatus = itemFormDto.getItemSellStatus();
      this.checkinDate = itemFormDto.getCheckinDate();
      this.checkoutDate = itemFormDto.getCheckoutDate();
   }

   public void removeStock(int stockNumber){
      int restStock = this.stockNumber - stockNumber;
      if(restStock<0){
         throw  new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 :" +this.stockNumber+")");
      }
      this.stockNumber = restStock;
   }

   public void addStock(int stockNumber){
      this.stockNumber += stockNumber;
   }


}
