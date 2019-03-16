/**
 * 
 */
package com.tw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tw.entity.ProductInventory;
import com.tw.pubsub.InventoryEventPuslisher;
import com.tw.repo.InventoryRepository;

/**
 * @author Ganesh Chaitanya Kale
 *
 */
@Service
public class InventoryService {
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	InventoryEventPuslisher inventoryEventPuslisher;
	
    @Value("${google.cloud.productservice.topicId}")
    private String TOPIC_NAME;

	public List<ProductInventory> getAllProducts() {
		return inventoryRepository.findAll();	
	}
	
	public boolean isProductInStock(String productName) {
		ProductInventory productInventory = inventoryRepository.findByProductName(productName);
		if(productInventory != null && productInventory.isInStock()) {
			if(productInventory.getQuantity() <=0) {
				productInventory.setInStock(false);
				try {
					inventoryEventPuslisher.publishMessage(TOPIC_NAME, "OutOfStock");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				productInventory.setQuantity(productInventory.getQuantity() -1);
				try {
					inventoryEventPuslisher.publishMessage(TOPIC_NAME, "InStock");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			inventoryRepository.save(productInventory);
			return inventoryRepository.findByProductName(productName).isInStock();
		} else {
			try {
				inventoryEventPuslisher.publishMessage(TOPIC_NAME, "OutOfStock");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public ProductInventory updateProductDesc(ProductInventory productInventory) {
		ProductInventory savedProductInventory = inventoryRepository.findByProductName(productInventory.getProductName());
		savedProductInventory.setProductDesc(productInventory.getProductDesc());
		return inventoryRepository.save(savedProductInventory);
	}
	
	public ProductInventory createProducts(ProductInventory productInventory) {
		return inventoryRepository.save(productInventory);
	}

}
