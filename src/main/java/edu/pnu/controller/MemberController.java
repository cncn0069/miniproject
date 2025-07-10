package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<?> memberInfo(Authentication authentication) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal(); 
		log.info("로그인 시도 사용자  = {}",user);
		try {
			log.info("로그인 성공 = {}",user);
			return ResponseEntity.ok(memberService.getMemberInfo(user.getUsername()));
		}catch (NullPointerException e) {
			// TODO: handle exception
			log.info("로그인 하지 않은 사용자.");
		}
		
		return null;
	}
	
	@GetMapping("/membersinfo")
	public ObjectDto membersInfo() {
		return new ObjectDeco(
				MainDto.builder()
				.members(memberService.getNicknamesInfo())
				.build()).getObjectDtoDeco();
	}
	
	@PostMapping("/api/useredit")
	public void userEdit(@RequestBody ObjectDto dto,Authentication authentication) {
		//토큰 인증정보와 dto user 정보가 같으면
		if(dto.getContent().getMember().getUsername().equals(authentication.getName())) {
			memberService.setMember(dto.getContent().getMember());
		}
	}
	
	@PostMapping("/api/user/delete")
	public void userEdit(Authentication authentication) {
		//토큰 인증정보와 dto user 정보가 같으면
		
			memberService.deleteMember(authentication.getName());
	}

}
//@AuthenticationPrincipal Authentication principal
//왜 안됐는지 질문