package edu.pnu.config.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.domain.Member;
import edu.pnu.service.LogService;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ObjectMapper mapper = new ObjectMapper();
		log.info("인증 프로세스 시작 ");
		try {
			Member m = mapper.readValue(request.getInputStream(), Member.class);
			log.info(m.getUsername());
			Authentication authToken = new UsernamePasswordAuthenticationToken(m.getUsername(), m.getPassword());
			return authenticationManager.authenticate(authToken);
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User)authResult.getPrincipal();
//		String token = JWT.create()
//				.withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.ACCESS_TOKEN_MESC))
//				.withClaim("username", user.getUsername())
//				.sign(Algorithm.HMAC256("edu.pnu.jwt"));
//		Cookie cookie = new Cookie("jwtToken", token);
//		cookie.setHttpOnly(true);
//		cookie.setSecure(false); // HTTPS 환경에서만 쿠키 전송됨
//		cookie.setPath("/");
//		cookie.setMaxAge((int)JWTUtil.ACCESS_TOKEN_MESC);
//		response.addCookie(cookie);
//		
//		
//		
//		cookie = new Cookie("userinfo", user.getUsername());
//		response.addCookie(cookie);
		//로그인 로그 저장

		
		String token = JWTUtil.getJWT(user.getUsername());
		
		response.addHeader(HttpHeaders.AUTHORIZATION,token);
		response.setStatus(HttpStatus.OK.value());
		log.info("인증 프로세스 성공 ");
	}
	

}
