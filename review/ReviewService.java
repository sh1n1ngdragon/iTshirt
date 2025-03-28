package com.shop.review;

import com.shop.board.BoardRepository;
import com.shop.board.BoardRequestDto;
import com.shop.board.BoardResponseDto;
import com.shop.board.SuccessResponseDto;
import com.shop.member.Member;
import com.shop.member.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ReviewService {
   private final MemberService memberservice;
   private final ReviewRepository reviewRepository;

   @Transactional(readOnly = true)
   public List<ReviewResponseDto> getPost() {
      return reviewRepository.findAllByOrderByUpdateTimeDesc().stream().map(ReviewResponseDto::new).toList();
   }


   @Transactional
   public ReviewResponseDto createPost(ReviewRequestDto requestDto){
      Member loggedInMember = memberservice.getLoggedInMember();
      Review review = new Review(requestDto);
      System.out.println("Saving Review: "+ review);
      review.setRegTime(LocalDateTime.now());
      review.setMember(loggedInMember);
      reviewRepository.save(review);
      Review savedReview = reviewRepository.findById(review.getNumber()).orElse(null);
      System.out.println("saved Review in DB : " +savedReview);

      return new ReviewResponseDto(review);
   }

 
   



   @Transactional
   public ReviewResponseDto getPost(Long number){
      return reviewRepository.findById(number).map(ReviewResponseDto::new).orElseThrow(
              () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
      );
   }

   @Transactional

   public ReviewResponseDto updatePost(Long id, ReviewRequestDto requestDto) throws Exception{
      Review review = reviewRepository.findById(id).orElseThrow(
              () ->new IllegalArgumentException("아이디가 존재하지 않습니다.")
      );
      review.update(requestDto);
      return new ReviewResponseDto(review);
   }

   @Transactional
   public SuccessResponseDto deletePost(Long id) throws Exception{
      Review review = reviewRepository.findById(id).orElseThrow(
              ()->new IllegalArgumentException("아이디가 존재하지 않습니다.")
      );
      reviewRepository.deleteById(id);
      return new SuccessResponseDto(true);
   }

   @Transactional
   public ReviewResponseDto incrementReadCount(Long id){
      Review review = reviewRepository.findById(id).orElseThrow(
              ()->new IllegalArgumentException("아이디가 존재하지 않습니다.")
      );
      review.incrementReadCount();
      return new ReviewResponseDto(review);
   }


   @Transactional
   public ReviewResponseDto incrementLikeCount(Long id){
      Review review = reviewRepository.findById(id).orElseThrow(
              ()->new IllegalArgumentException("아이디가 존재하지 않습니다.")
      );
      review.incrementLikeCount();
      return new ReviewResponseDto(review);
   }

}
