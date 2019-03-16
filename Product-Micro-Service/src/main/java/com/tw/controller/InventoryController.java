/**
 * 
 */
package com.tw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.entity.ProductInventory;
import com.tw.service.InventoryService;

/**
 * @author Ganesh Chaitanya Kale
 *
 */
@RestController
@RequestMapping( path = "/products")
public class InventoryController {

	@Autowired
	InventoryService inventoryService;
	
	@GetMapping
	public List<ProductInventory> getAllProducts(){
		return inventoryService.getAllProducts();
	}
	
	@PostMapping("/{productName}")
	public boolean isProductInStock(@PathVariable("productName") String productName) {
		return inventoryService.isProductInStock(productName);
	}
	
	@PutMapping
	public ProductInventory getSubs(@RequestBody ProductInventory productInventory) {
		return inventoryService.updateProductDesc(productInventory);
	}
	
	@PostMapping
	public ProductInventory createProduct(@RequestBody ProductInventory productInventory) {
		return inventoryService.createProducts(productInventory);
	}
	
	
}
