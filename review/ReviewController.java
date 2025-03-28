package com.shop.review;

import com.shop.board.BoardRequestDto;
import com.shop.board.BoardResponseDto;
import com.shop.board.BoardService;
import com.shop.board.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/review")
@RequiredArgsConstructor
public class ReviewController {

  //  private final BoardService boardService;
        private final ReviewService reviewService;
    /**
     * 게시판 목록 페이지
     */
    @GetMapping("/list")
    public String getPosts(Model model) {
        List<ReviewResponseDto> posts = reviewService.getPost();

        // regTime 포맷팅 추가
        posts.forEach(post -> {
            if (post.getRegTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                post.setFormattedRegTime(post.getRegTime().format(formatter));
            }
        });

        model.addAttribute("posts", posts);
        return "review/list"; // list.html 템플릿
    }

    /**
     * 게시글 작성 페이지
     */
    @GetMapping("/write")
    public String writePage() {
        return "review/write"; // write.html 템플릿
    }

    /**
     * 게시글 작성 요청 처리
     */
    @PostMapping("/write")
    public String createPost(@ModelAttribute ReviewRequestDto requestsDto) {
        reviewService.createPost(requestsDto);
        return "redirect:/review/list"; // 작성 완료 후 목록 페이지로 리다이렉트
    }

    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/{id:\\d+}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        ReviewResponseDto post = reviewService.getPost(id);
        System.out.println("Post: " + post);
        model.addAttribute("post", post);
        System.out.println("Post number: " + post.getNumber());
        return "review/detail"; // detail.html 템플릿
    }

    /**
     * 게시글 수정 요청 처리
     */
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        System.out.println("Edit page ID: " + id); // 디버깅 로그
        ReviewResponseDto post = reviewService.getPost(id);
        model.addAttribute("post", post);
        return "review/edit"; // edit.html 템플릿
    }

    @PostMapping("/{id}/edit")
    @ResponseBody
    public ResponseEntity<String> updatePostWithPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            // 요청 데이터에서 비밀번호와 업데이트할 내용을 추출
            String inputPassword = request.get("password");
            String subject = request.get("subject");
            String content = request.get("content");
            String name = request.get("name");

            // 기존 게시글 조회
            ReviewResponseDto post = reviewService.getPost(id);

            // 비밀번호 검증
            if (!post.getPassword().equals(inputPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
            }

            // 요청 데이터를 DTO에 매핑 후 업데이트
            ReviewRequestDto requestDto = new ReviewRequestDto();
            requestDto.setSubject(subject);
            requestDto.setContent(content);
            requestDto.setName(name);
            reviewService.updatePost(id,requestDto);

            return ResponseEntity.ok("게시글이 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 게시글 비밀번호 검증
     */
    @PostMapping("/{id}/verify")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String inputPassword = request.get("password");
            ReviewResponseDto post = reviewService.getPost(id);

            if (post.getPassword().equals(inputPassword)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }

    // <span style="color:red;">[변경됨] 조회수 증가 요청 처리 엔드포인트 추가</span>
    @PostMapping("/{id}/read")
    @ResponseBody
    public ResponseEntity<String> incrementReadCount(@PathVariable Long id) {
        reviewService.incrementReadCount(id);
        return ResponseEntity.ok("조회수가 증가되었습니다.");
    }

    // <span style="color:red;">[변경됨] 좋아요 수 증가 요청 처리 엔드포인트 추가</span>
    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<String> incrementLikeCount(@PathVariable Long id) {
        reviewService.incrementLikeCount(id);
        return ResponseEntity.ok("좋아요 수가 증가되었습니다.");
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public SuccessResponseDto deletePost(@PathVariable Long id) {
        try {
            System.out.println("삭제 요청 ID: " + id);
            reviewService.deletePost(id);
            System.out.println("게시글 삭제 완료 ID: " + id);
            return new SuccessResponseDto(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new SuccessResponseDto(false);
        }
    }

}
