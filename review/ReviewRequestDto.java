package com.shop.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRequestDto {
    private Long number;
    private String subject;
    private String content;
    private String name;
    private String password;
    private LocalDateTime regTime;
    private  LocalDateTime updateTime;
    private  int readCount;
    private int likeCount;
}
