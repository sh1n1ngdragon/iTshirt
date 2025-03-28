package com.shop.cart;

import com.shop.entity.BaseEntity;
import com.shop.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {
   @Id
   @GeneratedValue
   @Column(name = "cart_item_id")
   private Long id;
   @ManyToOne
   @JoinColumn(name = "cart_id")
   private Cart cart;
   @ManyToOne
   @JoinColumn(name = "item_id")
   private Item item;
   private int count;


   public static CartItem createCartItem (Cart cart, Item item, int count){
      CartItem cartItem =new CartItem();
      cartItem.setCart(cart);
      cartItem.setItem(item);
      cartItem.setCount(count);

      return cartItem;
   }

   public  void addCount(int count){this.count+=count;}

   public void updateCount(int count){this.count=count;}


}
