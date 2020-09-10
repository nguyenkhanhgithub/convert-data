package com.chozoi.convertdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class ConvertdataApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConvertdataApplication.class, args);
  }

}
