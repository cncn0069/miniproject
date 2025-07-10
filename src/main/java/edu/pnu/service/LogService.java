package edu.pnu.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.LoginLog;
import edu.pnu.domain.Member;
import edu.pnu.persistence.LoginLogRepository;
import edu.pnu.persistence.MemberRepository;

@Service
public class LogService {
	
	@Autowired
	LoginLogRepository loginLogRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	public void setloginLog(String username) {
		
		Member member = memberRepo.findById(username).get();
		
		loginLogRepo.save(LoginLog.builder()
				.username(member)
				.role(member.getRole())
				.enabled(member.isEnabled())
				.created_at(LocalDateTime.now())
				.build());
	}
}
