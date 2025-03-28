package com.shop.crawling;

import com.shop.constant.ItemSellStatus;
import com.shop.product.Product;
import com.shop.product.ProductDto;
import com.shop.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final ProductRepository productRepository;

    public List<ProductDto> getCrawlingData() {
        System.out.println("크롤링 데이터 수집 시작");
        List<ProductDto> productDtoList = new ArrayList<>();

        // 크롬 드라이버 경로 설정
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver-win64/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://travelmate.co.kr/product/list.html?cate_no=101");
            Thread.sleep(2000); // 로딩 대기

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            int scrollCount = 0;
            while (scrollCount < 5) {
                long lastHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight");
                jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.xans-record-")));

                long newHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight");
                if (newHeight == lastHeight) break;
                scrollCount++;
            }

            List<WebElement> elements = driver.findElements(By.cssSelector("li.xans-record-"));
            System.out.println("찾은 목록 수 : " + elements.size());

            for (WebElement element : elements) {
                try {
                    String subject = element.findElement(By.cssSelector(".description .name a span")).getText();

                    // 🔴 기존 코드: `price`가 String으로 저장됨
                    // String price = element.findElement(By.cssSelector(".description .spec .price")).getText();

                    // ✅ 수정된 코드: 숫자만 남기고 변환하여 저장
                    String priceStr = element.findElement(By.cssSelector(".description .spec .price")).getText();
                    int price = convertStringToInt(priceStr); // 🔥 price를 int로 변환

                    String image = element.findElement(By.cssSelector(".prdImg a img")).getAttribute("src");
                    String url = element.findElement(By.cssSelector(".prdImg a")).getAttribute("href");

                    ProductDto productDto = ProductDto.builder()
                            .subject(subject)
                            .price(price) // ✅ int 타입으로 저장
                            .image(image)
                            .url(url)
                            .sellStatus(ItemSellStatus.SELL)
                            .stockNumber(100)
                            .build();

                    productDtoList.add(productDto);
                } catch (Exception e) {
                    System.out.println("아이템 불러오는 중 오류 : " + e.getMessage());
                }
            }

            int count = 1;
            for (ProductDto dto : productDtoList) {
                System.out.println("저장된 크롤링 데이터 : " + (count++) + dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("크롤링 중 에러 : " + e.getMessage());
        } finally {
            driver.quit();
        }

        return productDtoList;
    }

    @Transactional
    public void saveCrawlingData(List<ProductDto> productDtoList) {
        List<Product> products = new ArrayList<>();

        for (ProductDto dto : productDtoList) {
            if (dto.getImage() == null || dto.getSubject() == null || dto.getUrl() == null) {
                System.out.println("유효하지 않은 DTO : " + dto);
                continue;
            }

            Product product = new Product();
            product.setSubject(dto.getSubject());
            product.setPrice(dto.getPrice()); // ✅ int 타입 price 저장
            product.setImageUrl(dto.getImage());
            product.setProductUrl(dto.getUrl());
            product.setSellStatus(dto.getSellStatus());
            product.setStockNumber(dto.getStockNumber());

            products.add(product);
        }

        productRepository.saveAll(products);
        System.out.println("Product 엔티티 저장 성공 : " + products);
    }

    // 🔥 price 변환 함수 추가
    private int convertStringToInt(String priceStr) {
        try {
            // 숫자 외의 문자 제거 (₩, 콤마 등 제거)
            String cleanedPrice = priceStr.replaceAll("[^0-9]", "");
            return Integer.parseInt(cleanedPrice);
        } catch (NumberFormatException e) {
            System.err.println("가격 변환 실패: " + priceStr);
            return 0; // 변환 실패 시 기본값 0 반환
        }
    }
}
