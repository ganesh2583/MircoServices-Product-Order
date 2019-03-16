/**
 * 
 */
package com.tw.entity;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Ganesh Chaitanya Kale
 *
 */

@Entity
public class OrderEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORD_SEQ")
	private Long orderId;
	
	private String productName;
	
	private String productDesc;

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
	
	
}
