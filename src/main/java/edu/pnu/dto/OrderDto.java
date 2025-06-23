package edu.pnu.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter@Setter@ToString@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	private String orderId;
	private String filePath;
	private Long totalPrice;
	private String username;
	private LocalDateTime createdAt;
	private Map<String,Integer> itemPrice;
}
