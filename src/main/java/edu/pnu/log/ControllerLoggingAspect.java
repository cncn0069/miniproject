package edu.pnu.log;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.domain.APILog;
import edu.pnu.persistence.APILogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    // 컨트롤러 패키지 대상 포인트컷 설정
    @Pointcut("execution(* edu.pnu.controller..*(..))")
    public void controllerPointcut() {}

    @Autowired
    public APILogRepository apiLogRepo;
    
    // 인증 정보 포함한 로깅 처리
    @Around("controllerPointcut()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
    	 ServletRequestAttributes sra = (ServletRequestAttributes) 
    		        RequestContextHolder.getRequestAttributes();
    	 
        HttpServletRequest request = sra.getRequest();
        
        HttpServletResponse response = sra.getResponse();
        		
        // 요청 정보 추출
        String method = request.getMethod();
        String uri = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "anonymous";

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 실제 컨트롤러 실행
        long executionTime = System.currentTimeMillis() - startTime;

        apiLogRepo.save(APILog.builder()
        		.ip_address(request.getRemoteAddr())
        		.username(username)
        		.api_endpoint(uri)
        		.http_method(method)
        		.status(response.getStatus())
        		.created_at(LocalDateTime.now())
        		.build()
        		);
        

        return result;
    }

    private String convertObjectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "JSON 변환 실패";
        }
    }
}
