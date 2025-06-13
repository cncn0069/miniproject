package edu.pnu.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.util.CustomMyUtil;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	@Autowired
	private MemberRepository mrp;
	@Lazy @Autowired
	private PasswordEncoder encoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("요청URL : " + request.getRequestURI());
		log.info("요청헤더Referer : " + request.getHeader("Referer"));
		
		OAuth2User user = (OAuth2User) authentication.getPrincipal();
		String username = CustomMyUtil.getUsernameFromOAuth2User(user);
		
		if (username == null) {
			log.error("on AuthenticationSuccess :  Oauth 응답에서 사용자 이름을 추출 할 수없음");
			throw new ServletException("cannot generate username from oauth2user");
		}
		log.info("OAuth2 인증 성공 : " + username);
		
		String nick = null;
		int lastIndexUnderbar = username.lastIndexOf("_");
		if (lastIndexUnderbar != -1 && username.length() >lastIndexUnderbar+5) {
			nick = username.substring(0, lastIndexUnderbar+6);
		}
		
		mrp.save(Member.builder()
				.username(username)
				.password(encoder.encode(""))
				.nickname(nick)
				.role(Arrays.asList(Role.ROLE_MEMBER, Role.ROLE_ADMIN))
				.enabled(true)
				.build()
				);
		
		String jwtToken = JWTUtil.getJWT(username);
		Cookie jwtCookie = new Cookie("jwtToken", URLEncoder.encode(jwtToken,"utf-8"));
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/");
		jwtCookie.setDomain("localhost");
		jwtCookie.setMaxAge((int)JWTUtil.ACCESS_TOKEN_MESC);
		response.addCookie(jwtCookie);
		response.sendRedirect("http://localhost:3000");
		log.info("OAuth2 인증 성공 종료 ");
//		response.sendRedirect("http://kdtminiproject.myvnc.com:3000/");
	}
}
