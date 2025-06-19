package edu.pnu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
public class SecurityConfig {
	private final OAuth2SuccessHandler successHandler;
	@Autowired
	private MemberRepository mrp;
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.cors(cors->cors.configurationSource(corSource()));
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
		
		

        http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()) // frameOptions sameOrigin 허용
        );
        
		
		return http.build();
	}
	private CorsConfigurationSource corSource() {
		CorsConfiguration cfg = new CorsConfiguration();
//		cfg.addAllowedOrigin("http://localhost:3000");  //set은 여러번 기존에 추가된 오리진 덮어씀
//		cfg.setAllowedOrigins(Arrays.asList(
//			    "http://localhost:3000",
//			    "http://10.125.121.186:3000"
//			    "https://kdtminiproject.myvnc.com:3000" //집에 서버 접속 안해도되서 주석해둠 넥스트에서 api로 접근하기 때문에 같은 로컬로 접속하는거라 안함
//			));
		cfg.setAllowCredentials(true);
		cfg.addAllowedOriginPattern(CorsConfiguration.ALL);
		cfg.addAllowedHeader(CorsConfiguration.ALL);
		cfg.addAllowedMethod(CorsConfiguration.ALL);//Get Post Delete Put 허용
//		cfg.setAllowCredentials(true);//인증정보를 허용 해당 메서드가 true 이면 addAllowedOrigin() 는* 사용할수 없고 특정 주소만가능
		cfg.addExposedHeader(HttpHeaders.AUTHORIZATION);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);
		return source;
	}

}
