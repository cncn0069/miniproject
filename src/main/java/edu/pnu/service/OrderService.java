package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.domain.OrderItem;
import edu.pnu.domain.OrderTable;
import edu.pnu.dto.OrderDto;
import edu.pnu.dto.OrderItemDto;
import edu.pnu.dto.OrderResponse;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.persistence.OrderItemRepository;
import edu.pnu.persistence.OrderRepository;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	OrderItemRepository orderItemRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	public void createOrder(OrderDto order) {
		
		Member member = memberRepo.getMemberByUsername(order.getUsername());
		
		if(member == null) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
		}
		OrderTable orderTable = OrderTable.builder()
				.orderId(order.getOrderId())
				.filePath(order.getFilePath())
				.member(member)
				.totalPrice(order.getTotalPrice())
				.createdAt(LocalDateTime.now())
				.build();
		orderRepo.save(orderTable);
		
		//아이템들 아이템 테이블에 저장
		for(String key : order.getItemPrice().keySet()) {
			orderItemRepo.save(OrderItem.builder()
					.orderId(orderTable)
					.itemName(key)
					.itemPrice(order.getItemPrice().get(key))
					.build());
		}
		
		
	}
	

	public List<OrderDto> readOrders(String username) {
		Member member = memberRepo.getMemberByUsername(username);
		
		if(member == null) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
		}
		
		List<OrderTable> orders = orderRepo.findByMember(member);
		
		List<OrderDto> results = new ArrayList<>();
			
		for(OrderTable order:orders) {
			List<OrderItem> items = orderItemRepo.getOrderItemByOrderId(order.getOrderId());
			results.add(OrderDto.builder()
			.orderId(order.getOrderId())
			.filePath(order.getFilePath())
			.totalPrice(order.getTotalPrice())
			.username(order.getMember().getUsername())
			.itemPrice(items.stream().collect(Collectors.toMap(OrderItem::getItemName, OrderItem::getItemPrice)))
			.createdAt(order.getCreatedAt())
			.build());
		}
		
		return results;
	}
	
	public Long readPrice(String orderId) {
		
		OrderTable orders = orderRepo.findById(orderId).get();
		
		return orders.getTotalPrice();
	}
	
	public OrderResponse getOrderItems(String orderId){
		
		List<OrderItem> orderitem = orderItemRepo.getOrderItemByOrderId(orderId);
		
		List<OrderItemDto> itemsDto = new ArrayList<>();
				
		for(OrderItem item:orderitem) {
			itemsDto.add(OrderItemDto.builder()
					.orderId(item.getOrderId().getOrderId())
					.itemName(item.getItemName())
					.itemPrice(item.getItemPrice())
					.build());	
		}

		
		OrderTable order = orderRepo.findById(orderId).get();
		//파일 패스를 찾아서
		//OrderResponse에 리스트와 함꼐 담아서 보냄
		OrderResponse orderResponse = OrderResponse.builder()
				.filePath(order.getFilePath())
				.orderItems(itemsDto)
				.build();
		return orderResponse;
	}
}
