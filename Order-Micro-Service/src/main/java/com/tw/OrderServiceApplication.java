package com.tw;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.tw.pubsub.OrderEventSubscriber;

@SpringBootApplication
@EnableHystrixDashboard
@EnableCircuitBreaker
public class OrderServiceApplication {
	
	@Autowired OrderEventSubscriber orderEventSubscriber;
	
	@Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
	
	@Bean
	public TaskExecutor taskExecutor() {
	    return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor) {
	    return new CommandLineRunner() {
	    	@Override
	        public void run(String... args) throws Exception {
	            executor.execute(orderEventSubscriber);
	        }
	    };
	}
}
