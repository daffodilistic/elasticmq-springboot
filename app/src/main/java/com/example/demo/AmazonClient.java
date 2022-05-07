package com.example.demo;

import java.util.List;

import javax.annotation.PostConstruct;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmazonClient {
    private AmazonSQS sqsClient;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    private String endpoint = "http://localhost:9324";
    private String queueUrl = "http://localhost:9324/000000000000/fifo-queue.fifo";
    private String messageGroupId = "group1";

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.sqsClient = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }

    public void sendMessage(String message) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(this.queueUrl)
                .withMessageBody(message)
                .withMessageGroupId(this.messageGroupId);
        this.sqsClient.sendMessage(send_msg_request);

    }

    public List<String> listQueues() {
        ListQueuesResult lq_result = this.sqsClient.listQueues();
        System.out.println("SQS Queue URLs:");
        for (String url : lq_result.getQueueUrls()) {
            System.out.println(url);
        }
        return lq_result.getQueueUrls();
    }
}