package edu.pnu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.OrderItem;
import edu.pnu.domain.OrderTable;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.persistence.OrderItemRepository;
import edu.pnu.persistence.OrderRepository;
import edu.pnu.service.EmailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository itemRepository;
	
	@Autowired
	private MemberRepository memberRepo;
	
	
	@GetMapping("email/order")
	public ResponseEntity<?> sendOrderIdEmail(@RequestParam String orderId,Authentication authentication) {
		//로그인 안햇으면 오류
		log.info("메일 전송 시작!");
		
		if(!authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		OrderTable orderTable = orderRepository.findById(orderId).get();
		
		//주문한 본인아니면 안됨
		if(authentication.getName().equals(orderTable.getMember().getUsername())) {
			
			List<OrderItem> orderItem = itemRepository.getOrderItemByOrderId(orderId);
			//메일 보내기
			emailService.sendPaymentInfoMail("cncn0069@naver.com", orderTable.getMember().getNickname(), orderTable.getTotalPrice(), orderTable.getCreatedAt(),orderItem);
		}
		
		
		return ResponseEntity.ok().build();
	}
}
