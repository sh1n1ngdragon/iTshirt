package com.shop.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
public class MemberFormDto {
   @NotBlank(message = "이름은 필수 입력 값입니다.")
   private String name;
   @NotEmpty(message = "이메일은 필수 입력 값입니다.")
   @Email(message = "이메일 형식으로 입력해주세요.")
   private String email;
   private String password;
   private String passwordConfirm;
   @NotEmpty(message = "주소는 필수 입력 값입니다.")
   private String address;
   @NotEmpty(message = "상세 주소를 입력해 주세요.")
   private String DetailAddress;
   @NotBlank(message = "연락처는 필수 입력 값입니다.")
   @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식(- 포함)을 입력해주세요.")
   private String tel;
}
