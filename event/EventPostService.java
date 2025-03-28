package com.shop.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPostService {

   private final EventPostRepository eventPostRepository;
   //private final String uploadDir = "uploads/";  // 저장 경로 설정
   private final String uploadDir = "C:/shop/event-uploads/";

   public EventPost savePost(EventPostDto dto) throws IOException {
      EventPost post = new EventPost();
      post.setTitle(dto.getTitle());
      post.setContent(dto.getContent());
      post.setAuthor(dto.getAuthor());  // 작성자 설정 추가

      List<EventImage> images = new ArrayList<>();

      if (dto.getImages() != null) {
         for (MultipartFile file : dto.getImages()) {
            if (!file.isEmpty()) {
               String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
               File destinationFile = new File(uploadDir + filename);
               
               // 디렉토리가 없으면 생성
               if (!destinationFile.getParentFile().exists()) {
                   destinationFile.getParentFile().mkdirs();
               }
               
               file.transferTo(destinationFile);

               EventImage image = new EventImage();
               image.setImageUrl(filename);  // 파일명만 저장
               image.setEventPost(post);
               images.add(image);
            }
         }
      }

      post.setImages(images);
      return eventPostRepository.save(post);
   }

   public EventPost savePost(EventPost post) {
      return eventPostRepository.save(post);
   }

   public List<EventPost> getAllPosts() {
      return eventPostRepository.findAll();
   }
   public Optional<EventPost> getPostById(Long id) {
      return eventPostRepository.findById(id);
   }

   public void deletePost(Long id) {
      EventPost post = eventPostRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("이벤트 게시글을 찾을 수 없습니다. ID: " + id));

      // 이미지 파일 삭제
      if (post.getImages() != null) {
          for (EventImage image : post.getImages()) {
              String imageUrl = image.getImageUrl();
              if (imageUrl != null) {
                  File imageFile = new File(uploadDir + imageUrl);
                  if (imageFile.exists()) {
                      imageFile.delete();
                  }
              }
          }
      }

      // 데이터베이스에서 게시글 삭제
      eventPostRepository.delete(post);
   }

}
