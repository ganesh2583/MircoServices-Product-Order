/**
 * 
 */
package com.tw.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tw.entity.ProductInventory;

/**
 * @author Ganesh Chaitanya Kale
 *
 */
public interface InventoryRepository extends JpaRepository<ProductInventory, Long> {

	ProductInventory findByProductName(String productName);
}
