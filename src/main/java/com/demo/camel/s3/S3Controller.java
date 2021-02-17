package com.demo.camel.s3;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3Controller {

  @Produce
  FluentProducerTemplate producer;

  public S3Controller(FluentProducerTemplate producer) {
    this.producer = producer;
  }

  @PostMapping("/test-s3")
  public ResponseEntity<Object> uploadTestFile() {

    Object response = producer.to("direct:startS3Upload")
        .request();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/test-s3")
  public ResponseEntity<String> downloadTestFile() {

    producer.to("direct:startS3Download").request();
    return ResponseEntity.ok("Download finished");
  }
}
