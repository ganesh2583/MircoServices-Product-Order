/**
 * 
 */
package com.tw.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tw.entity.OrderEntity;

/**
 * @author Ganesh Chaitanya Kale
 *
 */
public interface OrderRepo extends JpaRepository<OrderEntity, Long>{

}
