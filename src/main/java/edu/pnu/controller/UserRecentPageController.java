package edu.pnu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.service.UserRecentPageService;

@RestController
public class UserRecentPageController {
	
	@Autowired
	UserRecentPageService pageService;
	
	@GetMapping("/recentPages")
	public List<String> recentPages(Authentication authentication){
		return pageService.getRecentPages(authentication.getName());
	}
}
