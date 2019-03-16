package com.tw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.tw.pubsub.InventoryEventSubscriber;

@SpringBootApplication
public class ThoughtWorksWorkShopApplication {

	@Autowired
	InventoryEventSubscriber inventoryEventSubscriber;

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
	}

	public static void main(String[] args) {
		SpringApplication.run(ThoughtWorksWorkShopApplication.class, args);
	}

	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				executor.execute(inventoryEventSubscriber);
			}
		};
	}
}
