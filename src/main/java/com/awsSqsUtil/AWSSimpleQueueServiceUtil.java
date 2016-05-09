package com.awsSqsUtil;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.List;
import java.util.Map.Entry;

/**
 * Created by zheng on 5/5/16.
 */

public class AWSSimpleQueueServiceUtil {

    //private BasicAWSCredentials credentials;
    AWSCredentials credentials = null;

    private AmazonSQS sqs;

    private String simpleQueue = "PhotoQueue";

    String r_q_url="https://sqs.us-west-2.amazonaws.com/154194788582/CMPE282-FB";
    String s_q_url="https://sqs.us-west-2.amazonaws.com/154194788582/CMPE282-BF";


    //private static volatile  AWSSimpleQueueServiceUtil awssqsUtil = new AWSSimpleQueueServiceUtil();
    public AWSSimpleQueueServiceUtil() {

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs=new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);

    }

    public void sendMessage(String s_q, String message){
        sqs.sendMessage(new SendMessageRequest(s_q, "This is my message text."));

    }

    public List<Message> receiveMessage(String r_q){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(r_q);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
        }
        return messages;
    }
}
