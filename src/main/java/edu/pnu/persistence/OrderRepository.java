package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.Member;
import edu.pnu.domain.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, Long>{

	OrderTable findByMember(Member member);
}
