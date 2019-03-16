/**
 * 
 */
package com.tw.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Ganesh Chaitanya Kale
 *
 */

@Entity
public class ProductInventory {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROD_SEQ")
	private Long productId;
	
	private String productName;
	
	private String productDesc;
	
	private boolean isInStock;
	
	private Long quantity;

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public boolean isInStock() {
		return isInStock;
	}

	public void setInStock(boolean isInStock) {
		this.isInStock = isInStock;
	}
	
	
}
