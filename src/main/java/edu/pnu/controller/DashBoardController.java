package edu.pnu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.DashBoard;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.service.DashBoardService;

@RestController
public class DashBoardController {

	@Autowired
	DashBoardService dashBoardService;
	
	@PostMapping("api/posts")
	public void uploadDashBoard(@RequestBody DashBoardDto dto) {
		dashBoardService.uploadDashBoard(dto);
	}
	
	@GetMapping("api/posts")
	public List<DashBoard> getDashBoards(){
		
		return dashBoardService.getDashBoards();
	}
	
	@GetMapping("/api/posts/{id}")
	public DashBoard getDashBoard(@PathVariable Long id){
		
		return dashBoardService.getDashBoard(id);
	}
}
