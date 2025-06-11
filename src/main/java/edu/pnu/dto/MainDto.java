package edu.pnu.dto;

import java.util.List;

import edu.pnu.domain.Comment;
import edu.pnu.domain.DashBoard;
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
	private List<Comment> comments;
	private DashBoardDto dashboard;
	private List<DashBoard> dashboards;
	private DashBoard dsboard;
	
}
