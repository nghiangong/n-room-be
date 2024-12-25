package com.nghiangong.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private BrevoEmailService brevoEmailService;

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String toEmail) {
        brevoEmailService.sendEmail(toEmail, "Test Subject", "<h1>This is a test email</h1>");
        return "Email sent successfully!";
    }
}
