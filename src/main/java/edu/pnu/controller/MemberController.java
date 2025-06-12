package edu.pnu.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.MemberDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.service.MemberService;

@RestController
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@PostMapping("/memberinfo")
	public ObjectDto memberInfo(@AuthenticationPrincipal Principal principal) {

		return new ObjectDeco(
				MainDto.builder()
				.member(
						memberService.getMemberInfo(principal.getName())
				)
				.build())
				.getObjectDtoDeco();
	}
}
