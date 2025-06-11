package edu.pnu.dto;

import java.util.List;

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
	private DashBoardDto dashboard;
	private List<DashBoardDto> dashboards;
	private DashBoardDto dsboard;
}
