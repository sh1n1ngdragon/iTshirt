package com.shop.board;

import com.shop.review.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자 추가
public class Board{

   @Id
   @Column(name="board_no")
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long number; // 게시글 번호

   @Column(nullable = false, length = 100)
   private String subject; // 제목

   @Column(nullable = false)
   private String name; // 글쓴이

   @Lob
   @Column(nullable = false)
   private String content; // 내용

   @Column(nullable = false,updatable = false)
   private LocalDateTime regTime; // 등록일

   @LastModifiedDate // 변경 시 자동 저장
   private LocalDateTime updateTime; // 수정일

   private String password;

   @Column(nullable = false)
   private int readCount = 0; // 조회수, 기본값 0

   @Column(nullable = false)
   private int likeCount = 0; // 좋아요 수, 기본값 0



   // 추가 생성자
   public Board(BoardRequestDto requestDto) {
      this.subject = requestDto.getSubject();
      this.content = requestDto.getContent();
      this.name = requestDto.getName();
      this.regTime = LocalDateTime.now();
      this.password = requestDto.getPassword();
   }


   public void update(BoardRequestDto requestDto) {
      this.subject = requestDto.getSubject();
      this.content = requestDto.getContent();
      this.name = requestDto.getName();
      this.updateTime = LocalDateTime.now(); // 수정 시간 갱신
   }
   // 조회수 증가 메서드
   public void incrementReadCount() {
      this.readCount++;
   }

   // 좋아요 수 증가 메서드
   public void incrementLikeCount() {
      this.likeCount++;
   }




   public boolean checkPassword(String inputPassword){
      return this.password.equals(inputPassword);
   }
}
