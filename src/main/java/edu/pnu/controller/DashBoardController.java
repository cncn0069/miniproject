package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.service.DashBoardService;

@RestController
public class DashBoardController {

	@Autowired
	DashBoardService dashBoardService;
	
	@PostMapping("api/posts")
	public void uploadDashBoard(@RequestBody ObjectDto dto) {
		dashBoardService.uploadDashBoard(dto.getContent().getDashboard());
	}
	
	@GetMapping("api/posts")
	public ObjectDto getDashBoards(){
		
		MainDto data = MainDto.builder()
				.dashboards(dashBoardService.getDashBoards())
				.build();
		
		return new ObjectDeco(data).getObjectDtoDeco();
	}
	
	@GetMapping("/api/posts/{id}")
	public ObjectDto getDashBoard(@PathVariable Long id){
		
		MainDto data = MainDto.builder()
				.dsboard(dashBoardService.getDashBoard(id))
				.build();
		
		return new ObjectDeco(data).getObjectDtoDeco();
	}
}
