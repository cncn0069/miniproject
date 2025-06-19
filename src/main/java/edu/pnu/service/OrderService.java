package edu.pnu.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.domain.OrderTable;
import edu.pnu.dto.OrderDto;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.persistence.OrderRepository;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	public void createOrder(OrderDto order) {
		
		Member member = memberRepo.getMemberByUsername(order.getUsername());
		
		if(member == null) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
		}
		
		
		
		orderRepo.save(OrderTable.builder()
				.orderId(order.getOrderId())
				.filePath(order.getFilePath())
				.member(member)
				.totalPrice(order.getTotalPrice())
				.createdAt(LocalDateTime.now())
				.build());
	}
	
	public OrderDto readOrder(String username) {
		Member member = memberRepo.getMemberByUsername(username);
		
		if(member == null) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
		}
		
		OrderTable order = orderRepo.findByMember(member);
		
		return OrderDto.builder()
				.orderId(order.getOrderId())
				.filePath(order.getFilePath())
				.totalPrice(order.getTotalPrice())
				.username(order.getMember().getUsername())
				.createdAt(order.getCreatedAt())
				.build();
	}
}
