package com.nghiangong.email;

import com.nghiangong.entity.user.Tenant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BrevoEmailService {

    @Value("${brevo.api-key}")
    private String apiKey;  // API Key của Brevo

    private final RestTemplate restTemplate;

    public BrevoEmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        String url = "https://api.brevo.com/v3/smtp/email";  // Địa chỉ API gửi email của Brevo

        // Thiết lập các headers cho yêu cầu
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);  // Thêm API Key vào header
        headers.set("Content-Type", "application/json");

        // Tạo nội dung JSON cho email
        String bodyContent = "{"
                             + "\"sender\": { \"email\": \"nmn16102002@gmail.com\" },"
                             + "\"to\": [{ \"email\": \"" + toEmail + "\" }],"
                             + "\"subject\": \"" + subject + "\","
                             + "\"htmlContent\": \"" + body + "\""
                             + "}";

        // Tạo HttpEntity với dữ liệu yêu cầu và header
        HttpEntity<String> entity = new HttpEntity<>(bodyContent, headers);

        // Gửi yêu cầu HTTP POST đến API của Brevo
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Email sent successfully!");
        } else {
            System.out.println("Failed to send email.");
        }
    }

    public void sendAccount(Tenant tenant) {
        sendEmail(tenant.getEmail(),
                "Tài khoản truy cập",
                "<h3>username: " + tenant.getUsername() + "</h3><h3>password: " + 1 + "</h3>");
    }


}
