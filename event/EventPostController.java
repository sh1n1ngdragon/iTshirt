package com.shop.event;

import com.shop.event.EventPost;
import com.shop.event.EventPostDto;
import com.shop.event.EventPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventPostController {

   private final EventPostService eventPostService;

   // ✅ 1. 이벤트 목록 (list.html)
   @GetMapping
   public String listPosts(Model model) {
      List<EventPost> posts = eventPostService.getAllPosts();
      model.addAttribute("posts", posts);
      return "event/list";
   }

   // ✅ 2. 이벤트 작성 폼 (form.html)
   @GetMapping("/new")
   public String createPostForm(Model model) {
      model.addAttribute("eventPostDto", new EventPostDto());
      return "event/form";
   }

   // ✅ 3. 이벤트 저장 후 목록으로 리다이렉트
   @PostMapping("/new")
   public String createPost(@ModelAttribute EventPostDto eventPostDto) throws IOException {
      eventPostService.savePost(eventPostDto);
      return "redirect:/event";
   }

   // ✅ 4. 이벤트 상세 페이지 (detail.html)
   @GetMapping("/{id}")
   public String viewEvent(@PathVariable Long id, Model model) {
      Optional<EventPost> postOptional = eventPostService.getPostById(id);
      if (postOptional.isEmpty()) {
         return "redirect:/event";  // 이벤트가 없으면 목록으로 리다이렉트
      }
      
      EventPost post = postOptional.get();
      post.incrementReadCount();
      eventPostService.savePost(post);
      
      model.addAttribute("post", post);
      return "event/detail";  // detail.html 반환
   }

   // 이벤트 게시글 삭제
   @DeleteMapping("/{id}")
   @ResponseBody
   public String deletePost(@PathVariable Long id) {
      try {
         eventPostService.deletePost(id);
         return "게시글이 성공적으로 삭제되었습니다.";
      } catch (EntityNotFoundException e) {
         return "게시글을 찾을 수 없습니다.";
      } catch (Exception e) {
         return "게시글 삭제 중 오류가 발생했습니다.";
      }
   }
}

