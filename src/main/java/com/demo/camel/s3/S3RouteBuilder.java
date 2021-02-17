package com.demo.camel.s3;

import static org.apache.camel.Exchange.FILE_NAME;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3RouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    AWSCredentials awsCredentials = new BasicAWSCredentials("",
        "");

    AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

    AmazonS3 client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.EU_CENTRAL_1)
        .withCredentials(credentialsProvider)
        .build();

    bindToRegistry("client", client);

    from("direct:startS3Upload")
        .log("${routeId} started")
        .setHeader(S3Constants.KEY).constant("test.txt")
        .setBody().constant("test s3")
        .to("aws-s3:bluecode-clearing-test?amazonS3Client=#client");

    from("direct:startS3Download")
        .log("${routeId} started")
        .pollEnrich().simple("aws-s3://bluecode-clearing-test"
            + "?amazonS3Client=#client"
            + "&deleteAfterRead=false"
            + "&fileName=test.txt")
        .setHeader(FILE_NAME).header(S3Constants.KEY)
        .toD("file:/opt/camel-s3/downloads"); //this folder should exist and application should have access to it

  }
}
