package com.shop.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor


public class MemberController {

   private final MemberService memberService;
   private final PasswordEncoder passwordEncoder;


   @GetMapping(value = "/new")
   public String memberForm(Model model){
      model.addAttribute("memberFormDto",new MemberFormDto());
      return "member/memberForm";
   }

   @PostMapping(value = "/new")
   public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                            Model model){

      // @Valid 붙은 객체를 검사해서 결과에 에러가 있으면 실행
      if(bindingResult.hasErrors()){
         return "member/memberForm"; // 다시 회원가입으로 돌려보냅니다. GET
      }

      try{
         //Member 객체 생성
         Member member= Member.createMember(memberFormDto,passwordEncoder);
         memberService.saveMember(member);
      }
      catch (IllegalStateException e){
         model.addAttribute("errorMessage",e.getMessage());
         return "member/memberForm";
      }
      return "redirect:/";
   }

   @GetMapping(value = "/login")
   public String loginMember() { return "member/memberLoginForm";}

   @GetMapping(value = "/login/error")
   public String loginError(Model model){
      model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해 주세요");
      return "member/memberLoginForm";
   }

   @GetMapping("/user-info")
   @ResponseBody
   public Map<String, Object> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
      String email = userDetails.getUsername();
      Member member = memberService.findByEmail(email);
      
      Map<String, Object> userInfo = new HashMap<>();
      userInfo.put("email", member.getEmail());
      userInfo.put("name", member.getName());
      userInfo.put("role", member.getRole());
      userInfo.put("point", member.getPoint());
      userInfo.put("phone", member.getTel());
      userInfo.put("address", member.getAddress());
      userInfo.put("regDate", member.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      
      return userInfo;
   }

   // 마이페이지 뷰를 반환하는 새 엔드포인트 추가
   @GetMapping("/mypage")
   public String myPage() {
      return "member/userInfo";
   }

   /** 변경됨: 회원정보 수정 폼 요청 추가 **/
   @GetMapping("/update")
   public String memberUpdateForm(Model model) {
      Member member = memberService.getLoggedInMember();
      MemberFormDto memberFormDto = new MemberFormDto();
      memberFormDto.setEmail(member.getEmail());
      memberFormDto.setName(member.getName());
      memberFormDto.setTel(member.getTel());
      memberFormDto.setAddress(member.getAddress());
      model.addAttribute("memberFormDto", memberFormDto);
      return "member/memberUpdateForm";
   }

   /** 변경됨: 회원정보 수정 처리 추가 **/
   @PostMapping("/update")
   public String memberUpdate(@Valid @ModelAttribute("memberFormDto") MemberFormDto memberFormDto,
                            BindingResult bindingResult,
                            Model model) {
      if (bindingResult.hasErrors()) {
         return "member/memberUpdateForm";
      }

      try {
         Member member = memberService.getLoggedInMember();
         memberService.updateMember(member, memberFormDto, passwordEncoder);
         return "redirect:/members/mypage";
      } catch (IllegalStateException e) {
         model.addAttribute("errorMessage", e.getMessage());
         return "member/memberUpdateForm";
      }
   }

}
