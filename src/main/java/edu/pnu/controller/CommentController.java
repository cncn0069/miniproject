package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.persistence.APILogRepository;
import edu.pnu.service.CommentService;

@RestController
public class CommentController {

	@Autowired
	CommentService commentService;
	
	@Autowired
	APILogRepository apiLogRepo;
	
	@PostMapping("/api/comment/write")
	public void writeComment(@RequestBody ObjectDto dto) {
		
		commentService.writeComment(dto.getContent().getComment());
	}
	
	@PostMapping("/api/comment/read")
	public ObjectDto readComment(@RequestBody ObjectDto dto) {
		MainDto data = MainDto.builder().
				comments(commentService.readComment(dto.getContent().getComment()))
				.build();
		
		return new ObjectDeco(data).getObjectDtoDeco();
	}
	
	@PostMapping("/api/comment/delete")
	public void deleteComment(@RequestParam Long id) {
		commentService.deleteComment(id);
	}
}
