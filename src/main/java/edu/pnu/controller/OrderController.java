package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.dto.OrderDto;
import edu.pnu.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@PostMapping("/api/order/create")
	public void orderCreate(@RequestBody ObjectDto dto) {
		if (dto.getContent() == null || dto.getContent().getOrder() == null) {
	        log.error("Invalid request: order data is missing");
	    }
		
		orderService.createOrder(dto.getContent().getOrder());
	}
	
	@GetMapping("/api/order/reads")
	public ObjectDto orderReads(Authentication authentication) {
		return ObjectDto.builder()
				.content(MainDto.builder()
						.orders(orderService.readOrders(authentication.getName())).build()).build();
	}
	
	@GetMapping("/api/order/valid")
	public ObjectDto orderTotalPrice(@RequestParam String orderId) {
		return ObjectDto.builder()
				.content(MainDto.builder()
						.orderPrice(orderService.readPrice(orderId)).build()).build();
	}
	
	@GetMapping("/api/order/detail")
	public ObjectDto itemsRead(@RequestParam String orderId) {
		return ObjectDto.builder()
		.content(MainDto.builder()
				.orderResponse(orderService.getOrderItems(orderId)).build()).build();
	}
}
