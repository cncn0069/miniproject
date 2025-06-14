package edu.pnu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.persistence.CommentRepository;
import edu.pnu.persistence.DashBoardRepository;
import edu.pnu.persistence.MemberRepository;
@Component
public class MemberInit implements ApplicationRunner{
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	CommentRepository commentRepo;
	
	@Autowired
	DashBoardRepository dashBoardRepo;
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder(); 
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		memberRepository.save(Member.builder()
				.username("member1")
				.password(encoder.encode("abcd"))
				.nickname("nickname1")
				.enabled(true)
				.role(Arrays.asList(Role.ROLE_MEMBER, Role.ROLE_ADMIN))
				.created_at(LocalDateTime.now())
				.build());
		memberRepository.save(Member.builder()
				.username("member2")
				.password(encoder.encode("abcd"))
				.nickname("nickname2")
				.enabled(true)
				.role(Arrays.asList(Role.ROLE_MEMBER, Role.ROLE_ADMIN))
				.created_at(LocalDateTime.now())
				.build());
		
	
	}
	
}
