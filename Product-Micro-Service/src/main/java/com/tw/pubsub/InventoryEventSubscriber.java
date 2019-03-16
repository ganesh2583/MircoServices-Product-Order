package com.tw.pubsub;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.tw.service.InventoryService;

@Service
@Configurable
public class InventoryEventSubscriber implements Runnable {

  private static final String ORDER_PLACED = "OrderPlaced";
  
  @Value("${google.cloud.projectId}")
  private String PROJECT_ID;
  
  @Value("${google.cloud.productservice.subscriberId}")
  private String SUBSCRIPTION_ID;
  
  private Thread subscriberThread;
  
  @Autowired InventoryService inventoryService;

  private static final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<>();
  
  public InventoryEventSubscriber() {
	  this.subscriberThread = new Thread();
	  subscriberThread.run();
  }

  static class MessageReceiverExample implements MessageReceiver {

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
      messages.offer(message);
      consumer.ack();
    }
  }

  /** Receive messages over a subscription. */
  public void subscribeEvent() throws Exception {
    ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
        PROJECT_ID, SUBSCRIPTION_ID);
    Subscriber subscriber = null;
    try {
      // create a subscriber bound to the asynchronous message receiver
      subscriber =
          Subscriber.newBuilder(subscriptionName, new MessageReceiverExample()).build();
      subscriber.startAsync().awaitRunning();
      // Continue to listen to messages
      while (true) {
        PubsubMessage message = messages.take();
        System.out.println("Message Id: " + message.getMessageId());
        System.out.println("Data: " + message.getData().toStringUtf8());
        String eventString = message.getData().toStringUtf8();
        if(eventString.startsWith(ORDER_PLACED)) {
            String productName = eventString.substring(message.getData().toStringUtf8().indexOf(":")+1, message.getData().toStringUtf8().length());
            System.out.println("productName: " + productName);
            inventoryService.isProductInStock(productName);
        }
      }
    } finally {
      if (subscriber != null) {
        subscriber.stopAsync();
      }
    }
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			subscribeEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}