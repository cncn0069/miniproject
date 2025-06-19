package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.dto.OrderDto;
import edu.pnu.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@PostMapping("/order/create")
	public void orderCreate(ObjectDto dto) {
		orderService.createOrder(dto.getContent().getOrder());
	}
	
	@GetMapping("/order/read")
	public ObjectDto orderRead(Authentication authentication) {
		return ObjectDto.builder()
				.content(MainDto.builder()
						.order(orderService.readOrder(authentication.getName())).build()).build();
	}
}
