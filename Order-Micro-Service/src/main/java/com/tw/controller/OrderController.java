/**
 * 
 */
package com.tw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.entity.OrderEntity;
import com.tw.service.OrderService;

/**
 * @author Ganesh Chaitanya Kale
 *
 */

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired OrderService orderService;
	
	@PostMapping
	public OrderEntity createOrder(@RequestBody OrderEntity order) {
		return orderService.createOrder(order);
	}
	
	@PutMapping
	public OrderEntity updateOrderDesc(@RequestBody OrderEntity order) {
		return orderService.updateProductDesc(order);
	}
}
