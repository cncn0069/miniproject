package edu.pnu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.ItemDto;

@RestController
public class ItemController {

	@GetMapping("api/item")
	public ItemDto item() {
		return ItemDto.builder()
				.id((long)1)
				.name("홍길동")
				.price(1000)
				.currency("KRW")
				.build();
	}
}
