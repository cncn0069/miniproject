package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Member;
import edu.pnu.domain.OrderItem;
import edu.pnu.domain.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, String>{

	List<OrderTable> findByMember(Member member);
	@Query("select i from OrderItem i where orderId = :orderId")
	List<OrderItem> getByOrderId(OrderTable orderId);
}
