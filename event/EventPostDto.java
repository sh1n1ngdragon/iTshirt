package com.shop.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EventPostDto {
   private String title;
   private String content;
   private String author;
   private List<MultipartFile> images;
}
