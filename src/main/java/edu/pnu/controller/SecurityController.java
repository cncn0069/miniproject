package edu.pnu.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.domain.Member;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SecurityController {
	private final MemberService memberService;
	
	@GetMapping("/")
	public void index(HttpServletResponse response) throws IOException{
		response.sendRedirect("http://localhost:3000/");
//		response.sendRedirect("http://kdtminiproject.myvnc.com:3000/");
	}
	@GetMapping("/api/user")
	public ResponseEntity<?> jwtCallBack(HttpServletRequest request){
		log.info("SecuritController :  jwtCallBack");
		String jwtToken = null;
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		for(Cookie cookie :cookies) {
			if (!cookie.getName().equals("jwtToken")) continue; 
			try {
				jwtToken = URLDecoder.decode(cookie.getValue(),"utf-8");
				System.out.println("cookie token "+jwtToken);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		}
		log.info(jwtToken);
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtToken).build();
	}
	
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	

}
