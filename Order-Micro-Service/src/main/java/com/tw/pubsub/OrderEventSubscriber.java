package com.tw.pubsub;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

@Configurable
@Service
public class OrderEventSubscriber implements Runnable {

	@Value("${google.cloud.projectId}")
	private String PROJECT_ID;

	@Value("${google.cloud.productservice.subscriberId}")
	private String SUBSCRIPTION_ID;
  
  private Thread subscriberThread;

  private static final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<>();
  
  public OrderEventSubscriber() {
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