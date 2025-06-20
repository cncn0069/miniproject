package edu.pnu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
	@GetMapping("/api/item")
	public ResponseEntity<Map<String, Object>> sdfff() {
	    Map<String, Object> response = new HashMap<>();
	    response.put("id", "usss");
	    response.put("name", "uheif");
	    response.put("price", 3000);
	    response.put("currency", "390");

	    return ResponseEntity.ok().body(response);
	}

}
