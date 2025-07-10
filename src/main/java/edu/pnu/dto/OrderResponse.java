package edu.pnu.dto;

import java.time.LocalDateTime;
import java.util.List;
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
public class OrderResponse {
	private String filePath;
	private OrderDto order;
	private List<OrderItemDto> orderItems;
}
