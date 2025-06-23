package edu.pnu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.pnu.config.filter.JWTAuthenticationFilter;
import edu.pnu.config.filter.JWTAuthorizationFilter;
//import edu.pnu.config.OAuth2SuccessHandler;
import edu.pnu.persistence.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer{
	private final OAuth2SuccessHandler successHandler;
	@Autowired
	private MemberRepository mrp;
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
	
	@Value("${redirect.nextjs.ip}")
	private String nextjsIp;
	
	@Value("${redirect.cookie.next}")
	private String cookiNext;

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//		http.cors(cors->cors.configurationSource(corSource()));
		http.cors(Customizer.withDefaults());
		http.authorizeHttpRequests(security->security
				.requestMatchers("/member/**").authenticated()
				.requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/loginCheck/**").authenticated()
				.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers("/error").permitAll()
				.anyRequest().permitAll()
				);
		http.csrf(c->c.disable());
		http.formLogin(f->f.disable());
		http.httpBasic(b->b.disable());
		http.addFilterBefore(new JWTAuthorizationFilter(mrp),AuthorizationFilter.class);
		http.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()));
		http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		
		http.oauth2Login(o->o
				.loginPage("http://localhost:3000")
				.successHandler(successHandler)
				);
		
		http.logout(logout->logout
				.logoutUrl("/logout")
				.logoutSuccessHandler((request,response,authentication)->{
					SecurityContextHolder.clearContext();
					Cookie cookie = new Cookie("jwtToken", null);
					cookie.setHttpOnly(true);//JS에서 접근 불가 (보안 목적)
					cookie.setSecure(false);//	HTTPS에서만 동작하도록 설정
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					response.setStatus(HttpServletResponse.SC_OK);//HTTP 상태 코드를 200 OK로 설정
					response.setContentType("application/json");//응답 본문(Response Body)의 형식
					response.getWriter().write("{\"message\":\"로그아웃성공\"}");
					response.getWriter().flush();//버퍼에 저장된 내용을 강제로 클라이언트로 전송
					
				}).permitAll()
				);
		
		

        http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()) // frameOptions sameOrigin 허용
        );
        
		
		return http.build();
	}
//	private CorsConfigurationSource corSource(){
//		CorsConfiguration cfg = new CorsConfiguration();
//		cfg.addAllowedOrigin("http://localhost:3000");  //set은 여러번 기존에 추가된 오리진 덮어
//		cfg.setAllowedOrigins(Arrays.asList(
//			    "http://localhost:3000",
//			    "http://10.125.121.176:3000",
//			    ngrokIp
//			    "http://10.125.121.186:3000"
//			    "https://kdtminiproject.myvnc.com:3000" //집에 서버 접속 안해도되서 주석해둠 넥스트에서 api로 접근하기 때문에 같은 로컬로 접속하는거라 안함
//			));
//		cfg.setAllowCredentials(true);
//		cfg.addAllowedOriginPattern(CorsConfiguration.ALL);
//		cfg.addAllowedHeader(CorsConfiguration.ALL);
//		cfg.addAllowedMethod(CorsConfiguration.ALL);//Get Post Delete Put 허용
//		cfg.setAllowCredentials(true);//인증정보를 허용 해당 메서드가 true 이면 addAllowedOrigin() 는* 사용할수 없고 특정 주소만가능
//		cfg.addExposedHeader(HttpHeaders.AUTHORIZATION);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", cfg);
//		return source;
//	}
	
	@Value("${file.upload.directory}")
	private String filepath;
	
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry
			.addResourceHandler("/images/**")
			.addResourceLocations("file:///C:/upload/images/");

	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	    .allowedOrigins(
	    		"http://10.125.121.176:3000",
	    		nextjsIp,
	    		cookiNext,
	    		"http://localhost:3000",
	    		"http://10.125.121.186:3000",
	    		"https://kdtminiproject.myvnc.com:3000"
	    		)
	    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
	    .allowCredentials(true)
	    .exposedHeaders("Authorization");

	}
//	registry.addMapping("/api/**")
//    .allowedOrigins("http://10.125.121.176:3000",ngrokIp,"http://localhost:3000")
//    .allowedMethods("GET", "POST", "PUT", "DELETE")
//    .allowCredentials(true);
//	
	@Bean
	public StrictHttpFirewall httpFirewall() {
	    StrictHttpFirewall firewall = new StrictHttpFirewall();
	    firewall.setAllowUrlEncodedDoubleSlash(true); // "//" 허용
	    return firewall;
	}

}
