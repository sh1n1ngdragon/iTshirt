package com.shop.crawling;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CrawlingDto {
    private String image;
    private int price; // ✅ 기존: String → 변경: int (숫자로 변환 후 저장)
    private String subject;
    private String detail;
    private String url;

    // 🔥 크롤링할 때 price를 String에서 int로 변환
    public static CrawlingDto create(String image, String priceStr, String subject, String detail, String url) {
        return CrawlingDto.builder()
                .image(image)
                .price(convertStringToInt(priceStr)) // 🔥 가격을 숫자로 변환 후 저장
                .subject(subject)
                .detail(detail)
                .url(url)
                .build();
    }

    // 🔥 String 가격을 숫자로 변환하는 메서드 추가
    private static int convertStringToInt(String priceStr) {
        try {
            return Integer.parseInt(priceStr.replaceAll("[^0-9]", "")); // ✅ "₩10,000" → "10000" 변환
        } catch (NumberFormatException e) {
            System.err.println("가격 변환 실패: " + priceStr);
            return 0; // 변환 실패 시 기본값 0 반환
        }
    }
}
