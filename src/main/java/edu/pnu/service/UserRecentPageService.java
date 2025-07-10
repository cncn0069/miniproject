package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import edu.pnu.domain.UserRecentPage;
import edu.pnu.persistence.UserRecentPageRepository;

@Service
public class UserRecentPageService {
	@Autowired
	UserRecentPageRepository pageRepo;
	
	public List<String> getRecentPages(String username){
		return pageRepo.getAllDashIdByUserName(username);
	}
	
	public void setRecentPage(String username,Long dashId){
		pageRepo.save(UserRecentPage.builder()
				.username(username)
				.dashId(dashId)
				.createdAt(LocalDateTime.now())
				.build());
	}
}
