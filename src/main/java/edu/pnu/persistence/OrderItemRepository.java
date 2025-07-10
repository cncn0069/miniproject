package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
	@Query("select i from OrderItem i where i.orderId.orderId = :orderId")
	List<OrderItem> getOrderItemByOrderId(String orderId);
}
