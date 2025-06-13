package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@GetMapping({"/loged-in/user","/memberinfo"})
	public ObjectDto memberInfo(Authentication authentication) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			User user = (User)authentication.getPrincipal(); 
			
			return new ObjectDeco(
					MainDto.builder()
					.member(
							memberService.getMemberInfo(user.getUsername())
					)
					.build())
					.getObjectDtoDeco();
		}catch (NullPointerException e) {
			// TODO: handle exception
			log.info("로그인 하지 않은 사용자.");
		}
		
		return null;
	}
	
}
//@AuthenticationPrincipal Authentication principal
//왜 안됐는지 질문