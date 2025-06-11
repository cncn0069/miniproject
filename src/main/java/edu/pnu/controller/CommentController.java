package edu.pnu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.Comment;
import edu.pnu.dto.CommentDto;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.service.CommentService;

@RestController
public class CommentController {

	@Autowired
	CommentService commentService;
	
	@PostMapping("/api/comment/write")
	public void writeComment(@RequestBody CommentDto dto) {
		
		commentService.writeComment(dto);
	}
	
	@PostMapping("/api/comment/read")
	public List<Comment> readComment(@RequestBody DashBoardDto dto) {
		System.out.println("comment");
		return commentService.readComment(dto);
	}
}
