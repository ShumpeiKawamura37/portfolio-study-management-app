package com.portfolio.study_management_app.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class TimeConfig {
  public static final ZoneId APP_ZONE = ZoneId.of("Asia/Tokyo");

  @Bean
  public Clock clock() {
    return Clock.system(APP_ZONE);
  }
}
