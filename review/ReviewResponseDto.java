package com.shop.review;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class ReviewResponseDto {
   @Id
   @Column(name = "board_no")
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long number;

   private String subject;

   private String content;
   private String name;
   private LocalDateTime regTime;
   private LocalDateTime updateTime;
   private String password;
   private String formattedRegTime; // 추가
   private int readCount; // 조회수
   private int likeCount; // 좋아요 수

   public ReviewResponseDto(Review entity) {
      this.number = entity.getNumber();
      this.subject = entity.getSubject();
      this.content = entity.getContent();
      this.name = entity.getName();
      this.password = entity.getPassword();
      this.regTime = entity.getRegTime();
      this.updateTime = entity.getUpdateTime();
      this.readCount = entity.getReadCount();
      this.likeCount = entity.getLikeCount();
   }
}