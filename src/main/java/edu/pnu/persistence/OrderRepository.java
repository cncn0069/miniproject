package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.Member;
import edu.pnu.domain.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, String>{

	List<OrderTable> findByMember(Member member);
}
