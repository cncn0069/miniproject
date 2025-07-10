package edu.pnu.controller;



import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.oauth2.sdk.Role;

import edu.pnu.config.SecurityConfig;
import edu.pnu.domain.Member;

import edu.pnu.persistence.MemberRepository;

@RestController
public class RegisterConroller {

    private final SecurityConfig securityConfig;
	@Autowired
	private MemberRepository mrp;
	@Autowired
	private PasswordEncoder encoder = new BCryptPasswordEncoder();


    RegisterConroller(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }
	

	@PostMapping("/registeration")
	public ResponseEntity<?> registerUser(@RequestBody Member membersEntity) {
		System.out.println("post 회원가입 요청 결과 : " );
		if (mrp.existsById(membersEntity.getUsername())) {
			return ResponseEntity.ok("이미 존재하는 아이디 입니다.");
		}else {
			Member m = Member.builder()
					.username(membersEntity.getUsername())
					.password(encoder.encode(membersEntity.getPassword()))
					.nickname(membersEntity.getNickname())
					.role(membersEntity.getRole())
					.address(membersEntity.getAddress())
					.enabled(true)
					.created_at(LocalDateTime.now())
					.build();
			
			mrp.save(m);
			
			return ResponseEntity.ok("회원가입 성공");
		}
		
	}
	
}
