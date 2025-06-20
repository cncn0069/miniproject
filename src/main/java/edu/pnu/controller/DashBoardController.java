package edu.pnu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.service.DashBoardService;
import edu.pnu.service.UserRecentPageService;

@RestController
public class DashBoardController {

	@Autowired
	DashBoardService dashBoardService;
	
	@Autowired
	UserRecentPageService pageService;
	
	@PostMapping("api/post/upload")
	public void uploadDashBoard(@RequestBody ObjectDto dto) {
		dashBoardService.uploadDashBoard(dto.getContent().getDashboard());
	}
	
	@PostMapping("api/post/insert")
	public void insertDashBoard(@RequestBody ObjectDto dto) {
		dashBoardService.insertDashBoard(dto.getContent().getDashboard());
	}
	
	@GetMapping("api/posts")
	public ResponseEntity<?> getDashBoards(
			@RequestParam (name="page",defaultValue= "1")int pageNum
			,@RequestParam (name="size",defaultValue= "10")	int pageSize
			,@RequestParam (defaultValue = "latest")String method
			,@RequestParam (defaultValue = "")String q
			,@RequestParam(name="writer")String writer){
		
		MainDto data = MainDto.builder()
				.dashResponseDto(dashBoardService.getDashBoards(pageNum,pageSize,method,q,writer))
				.build();
		
		return ResponseEntity.ok(new ObjectDeco(data).getObjectDtoDeco());
	}
	
	@GetMapping("api/post/{id}")
	public ResponseEntity<?> getDashBoard(@PathVariable Long id,Authentication authentication){

		if(authentication != null) {
			pageService.setRecentPage(authentication.getName(), id);
		}
		
		MainDto data = MainDto.builder()
				.dashboard(dashBoardService.getDashBoard(id))
				.build();
		
		return ResponseEntity.ok(new ObjectDeco(data).getObjectDtoDeco());
	}
	
	@PostMapping("api/post/delete")
	public void deleteDashBoard(@RequestParam Long dashId) {
		dashBoardService.deleteDashBoard(dashId);
	}
	
	@GetMapping("writersinfo")
	public List<String> writersInfo(){
		return dashBoardService.getWritersInfo();
	}
}
