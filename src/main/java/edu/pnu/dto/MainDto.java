package edu.pnu.dto;

import java.util.List;

import edu.pnu.domain.Furniture;
import edu.pnu.domain.OrderItem;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Data
public class MainDto {
	private CommentDto comment;
	private List<CommentDto> comments;
	private DashResponseDto dashResponseDto;
	private DashBoardDto dashboard;
	private MemberDto member;
	private List<MemberDto> members;
	private List<List<Furniture>> furnitures;
	private OrderDto order;
	private List<OrderDto> orders;
	private OrderResponse orderResponse;
	private Long orderPrice;
}
