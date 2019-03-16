/**
 * 
 */
package com.tw.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tw.entity.OrderEntity;
import com.tw.pubsub.OrderEventPuslisher;
import com.tw.repo.OrderRepo;

/**
 * @author Ganesh Chaitanya Kale
 *
 */
@Service
public class OrderService {

	@Autowired OrderRepo orderRepo;
	
	@Autowired OrderEventPuslisher orderEventPuslisher;
	
	@Autowired RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Value("${google.cloud.productservice.topicId}")
    private String TOPIC_NAME;
	
	public OrderEntity createOrder(OrderEntity order) {
		try {
			orderEventPuslisher.publishMessage(TOPIC_NAME, "OrderPlaced:"+order.getProductName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderRepo.save(order);
	}
	
    @HystrixCommand(fallbackMethod = "updateProductDesc_Fallback")
	public OrderEntity updateProductDesc(OrderEntity order) {
		String productPayload = "{\"productName\":\""+order.getProductName()+"\",\"productDesc\":\""+order.getProductDesc()+"\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(productPayload, headers);
		restTemplate.exchange("http://localhost:8080/products", HttpMethod.PUT, entity, Object.class);
		return order;
	}
    
    public OrderEntity updateProductDesc_Fallback(OrderEntity order) {
		return order;
	}
}
