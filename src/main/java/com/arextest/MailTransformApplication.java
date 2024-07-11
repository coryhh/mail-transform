package com.arextest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.arextest.mail"})
public class MailTransformApplication {

  public static void main(String[] args) {
    SpringApplication.run(MailTransformApplication.class, args);
  }
}
