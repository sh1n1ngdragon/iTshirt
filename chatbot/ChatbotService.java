package com.shop.chatbot;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ChatbotService {
   private final Random r = new Random();

   private final Map<String, String[]> responses = new HashMap<>();

   public ChatbotService() {
      responses.put("시간", new String[]{
              "고객 센터 운영시간은 평일 09:00~18:00입니다.",
              "고객센터는 평일 09:00~18:00 까지 입니다."
      });

      responses.put("위치", new String[]{
              "위치는 인천공항 제1 터미널 14번 게이트에 있습니다.",
              "위치는 인천공항 제1 여객터미널 14번 게이트에 위치해있습니다."
      });

      responses.put("연락처", new String[]{
              "연락은 032-1234-5678로 부탁드려요!",
              "전화번호는 032-1234-5678이에요!",
              "032-1234-5678로 연락 주세요!"
      });

      responses.put("감사", new String[]{
              "도와드릴 수 있어서 기뻐요!",
              "감사합니다! 다음에도 불러주세요!",
              "언제든지 도와드릴게요!"
      });

      responses.put("인사", new String[]{
              "안녕하세요! 무엇을 도와드릴까요?",
              "안녕! 무슨 일이야?",
              "제가 도와줄게요!",
              "원하는 게 뭐야? 말만 해!"
      });

      responses.put("서비스", new String[]{
              "저희는 다음과 같은 서비스를 제공해요:\n- 패키지 여행 상품\n- 여행에 필요한 물품\n- 여행지 정보",
              "저희의 주요 서비스는:\n- 패키지 여행 상품\n- 여행에 필요한 물품\n- 여행지 정보",
              "저희는 다양한 서비스를 제공하고 있어요!\n- 패키지 여행 상품\n- 여행에 필요한 물품\n- 여행지 정보"
      });

      responses.put("이해못한답변", new String[]{
              "무슨 뜻인지 모르겠어요... 다시 물어봐 주세요!",
              "이해 못했어요... 다른 질문 부탁드려요!",
              "잘 모르겠어요... 다시 말씀해 주세요!",
              "뭐라고요? 저도 모르겠어요!"
      });

      responses.put("환불", new String[]{
              "환불 정책은 상품마다 다릅니다. 일반적인 기준은 다음과 같습니다.\n" +
                      "- 출발 30일 전: 전액 환불\n" +
                      "- 출발 15~29일 전: 10% 취소 수수료\n" +
                      "- 출발 7~14일 전: 30% 취소 수수료\n" +
                      "- 출발 1~6일 전: 50% 취소 수수료\n",
              "환불 정책은 상품마다 상이하오니 고객센터로 문의바랍니다."
      });

      responses.put("필수", new String[]{
              "- 여권 & 비자 준비 완료?\n" +
                      "- 유로(EUR) 환전 미리 해두기\n" +
                      "- 포켓와이파이 or 유심 준비\n" +
                      "- 전압 변환 어댑터 (220V & 110V 체크)\n" +
                      "- 날씨에 맞는 옷 (겨울철은 따뜻한 옷 필수!)",
              "자세한 사항은 고객센터로 문의바랍니다."
      });
   }

   public String getResponse(String userInput) {
      for (Map.Entry<String, String[]> entry : responses.entrySet()) {
         String keyword = entry.getKey();
         String[] replies = entry.getValue();

         if (userInput.contains(keyword)) {
            return replies[r.nextInt(replies.length)];
         }
      }
      return responses.get("이해못한답변")[r.nextInt(responses.get("이해못한답변").length)];
   }
}
