package com.shop.event;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventImage {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String imageUrl;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "event_post_id")
   private EventPost eventPost;
}
