package edu.pnu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter@Setter@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
	private String orderId;
	private String itemName;
	private Integer itemPrice;
}
