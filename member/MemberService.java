package com.shop.member;

import com.shop.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor // final, @NonNull 변수에 붙으면 자동 주입(Autowired)을 해줍니다.
public class MemberService implements UserDetailsService {
   private final MemberRepository memberRepository; //자동 주입됨

   public Member saveMember(Member member) {
      validateDuplicateMember(member);
      return memberRepository.save(member); // 데이터베이스에 저장을 하라는 명령
   }
   private void validateDuplicateMember(Member member) {
      Member findMember;
      findMember = memberRepository.findByEmail(member.getEmail());
      if (findMember != null) {
         throw new IllegalStateException("이미 가입된 회원입니다."); // 예외 발생
      }
      findMember = memberRepository.findByTel(member.getTel());
      if (findMember != null) {
         throw new IllegalStateException("이미 가입된 전화번호입니다."); // 예외 발생
      }
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      Member member = memberRepository.findByEmail(email);

      if(member == null){
         throw new UsernameNotFoundException(email);
      }
      //빌더패턴
      return User.builder().username(member.getEmail())
              .password(member.getPassword())
              .roles(member.getRole().toString())
              .build();
   }
   public Member getLoggedInMember() {
      // SecurityContextHolder에서 인증 정보 가져오기
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !authentication.isAuthenticated()) {
         throw new IllegalStateException("로그인한 사용자가 없습니다.");
      }

      // Principal 객체에서 이메일(또는 username) 가져오기
      String email = authentication.getName();

      // 이메일로 Member 조회
      return memberRepository.findByEmail(email);
   }

   /** 변경됨: 회원정보 수정 메서드 추가 **/
   public void updateMember(Member member, MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
      // 이름, 연락처, 주소 업데이트
      member.setName(memberFormDto.getName());
      member.setTel(memberFormDto.getTel());
      member.setAddress(memberFormDto.getAddress());

      // 비밀번호가 입력된 경우에만 업데이트
      if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()) {
         if (memberFormDto.getPasswordConfirm() == null || memberFormDto.getPasswordConfirm().isEmpty()) {
            throw new IllegalStateException("비밀번호 확인을 입력해주세요.");
         }
         if (!memberFormDto.getPassword().equals(memberFormDto.getPasswordConfirm())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
         }
         if (memberFormDto.getPassword().length() < 8 || memberFormDto.getPassword().length() > 16) {
            throw new IllegalStateException("비밀번호는 8자 이상, 16자 이하로 입력해주세요.");
         }
         member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
      }

      memberRepository.save(member);
   }
   // 이메일로 사용자 조회
   public Member findByEmail(String email) {
      System.out.println("Searching for user with email : " + email);
      Member member = memberRepository.findByEmail(email);
      if (member == null) {
         throw new UsernameNotFoundException("User not found with email: " + email);
      }
      return member;
   }

   // 관리자 조회
   public Member findAdmin() {
      return memberRepository.findByRole(Role.ADMIN)
              .orElseThrow(() -> new IllegalStateException("Admin not found"));
   }


}
