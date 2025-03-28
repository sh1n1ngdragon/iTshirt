package com.shop.crawling;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "crawling")
@Getter
@Setter
public class Crawling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String subject;


   @Column(nullable = false)

   private String detail;

    @Column(length = 12000)
   private String url;
}
