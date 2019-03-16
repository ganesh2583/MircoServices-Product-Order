package com.tw.pubsub;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

@Service
public class InventoryEventPuslisher {

  @Value("${google.cloud.projectId}")
  private String PROJECT_ID;

  /** Publish messages to a topic.
   * @param args topic name, number of messages
   */
  public void publishMessage(String topicId, String message) throws Exception {
    ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
    Publisher publisher = null;
    List<ApiFuture<String>> futures = new ArrayList<>();
    try {
      // Create a publisher instance with default settings bound to the topic
      publisher = Publisher.newBuilder(topicName).build();
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
            .setData(data)
            .build();
        ApiFuture<String> future = publisher.publish(pubsubMessage);
        futures.add(future);
    } finally {
      // Wait on any pending requests
      List<String> messageIds = ApiFutures.allAsList(futures).get();
      for (String messageId : messageIds) {
        System.out.println(messageId);
      }
      if (publisher != null) {
        // When finished with the publisher, shutdown to free up resources.
        publisher.shutdown();
      }
    }
  }
}