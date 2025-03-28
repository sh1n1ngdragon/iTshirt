package com.shop.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventPost {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String title;
   private String content;
   private String author;
   private int readCount;

   @OneToMany(mappedBy = "eventPost", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<EventImage> images;

   // ✅ 등록일 필드 추가
   @CreationTimestamp // Hibernate가 자동으로 현재 시간 저장
   private LocalDateTime regTime;

   public void incrementReadCount() {
       this.readCount++;
   }
}
