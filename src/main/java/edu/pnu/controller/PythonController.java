package edu.pnu.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.ApiResponseDTO;
import edu.pnu.dto.ImagePermitRequestDTO;
import edu.pnu.dto.ImagePermitResponseDTO;
import edu.pnu.dto.ImageProcessResultDTO;
import edu.pnu.dto.ImageUploadRequestDTO;
import edu.pnu.dto.ImageUploadResponseDTO;
import edu.pnu.dto.IndexedFurnitureList;
import edu.pnu.service.ImagePermisionService;
import edu.pnu.service.ImageProcessService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class PythonController {
	@Autowired
	private ImageProcessService imageProcessService;
	@Autowired
	private ImagePermisionService imagePermisionService;
	/* 추후 /member/imgToPython으로 변경필요  @RequestMapping(" /member" ) auth객체도 필요할듯?*/
	
	//프론트에서 이미지 받기 비동기 통신 권한 인증 처리는 비동기 및 SecurityContext 전파 문제로 해결되지 않았음
	@PostMapping("/api/inference") 
	public Mono<ResponseEntity<ApiResponseDTO<ImageUploadResponseDTO>>> arriveImageFromFront(
			@ModelAttribute ImageUploadRequestDTO imageUploadRequestDTO,
			@AuthenticationPrincipal UserDetails userDetails
			) throws IOException{
		String username = userDetails.getUsername();
		log.info("Next.js 로부터 이미지 업로드 요청 도착 - username: {}, 로그인 유저 :{},파일명: {}, 크기: {} bytes",
		        imageUploadRequestDTO.getUsername(),
		        username,
		        imageUploadRequestDTO.getImage().getOriginalFilename(),
		        imageUploadRequestDTO.getImage().getSize());
		
		imageUploadRequestDTO.setUsername(username);
		return  imageProcessService.sendImageToFastApi(imageUploadRequestDTO); //메세지받는것 까지는 동기 처리
	}
	
	//프론트에서 이미지 받기 동기 통신
	@PostMapping("/member/api/inference/block") 
	public ResponseEntity<ApiResponseDTO<ImageUploadResponseDTO>> arriveImageFromFront2(
			@ModelAttribute ImageUploadRequestDTO imageUploadRequestDTO,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestHeader(value = "User-Agent", required = false)  String userAgent,
			HttpServletRequest request
			) throws IOException{
		
		log.info(">>> 요청 도착 - URI: {}", request.getRequestURI());
		log.info(">>> 요청 Authorization 헤더: {}", request.getHeader("Authorization"));
		log.info(">>> 요청 User-Agent: {}", request.getHeader("User-Agent"));
		log.info("Next.js 로부터 이미지 업로드 요청 도착 - username: {}, 파일명: {}, 크기: {} bytes",
		        imageUploadRequestDTO.getUsername(),
		        imageUploadRequestDTO.getImage().getOriginalFilename(),
		        imageUploadRequestDTO.getImage().getSize());
		
		String token = request.getHeader("Authorization");
		String username = userDetails.getUsername();
		
		imageUploadRequestDTO.setUsername(username);
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		
		log.info("Authorization 헤더에서 토큰 수신됨: {}", token);
		return  imageProcessService.sendImageToFastApi2(imageUploadRequestDTO,currentAuth,token); //메세지받는것 까지는 동기 처리
	}
	
	//이미지처리결과 리턴
	@GetMapping("/api/inference/{jobid}/result")
	public Mono<ResponseEntity<ApiResponseDTO<ImageProcessResultDTO>>> getImageResult(@PathVariable String jobid) {
		log.info("Next.js 로 결과 이미지 반환");
		return imageProcessService.getImageFromFastApi(jobid);
	}
	
	//선택 결과 DB조회
	@PostMapping("/api/inference/{jobid}/permission")
	public Mono<ApiResponseDTO<ResponseEntity<List<IndexedFurnitureList>>>> arrivepermitFromFront(@PathVariable String jobid,@RequestBody ImagePermitRequestDTO body){
		log.info("Next.js 로 부터 이미지 승인");
		return imagePermisionService.postImagepermit(jobid ,body);
	}
	
	//최종 결제 시 마스킹 요청
	@PostMapping("/api/inference/{jobid}/permission/final")
	public Mono<ApiResponseDTO<ImagePermitResponseDTO>> arrivepaymentFromFront(@PathVariable String jobid,@RequestBody ImagePermitRequestDTO body){
		log.info("Next.js 로 부터 최종 결제 데이터 {}", body);
		return imagePermisionService.permitImageMaksing(jobid ,body);
	}
	
	@GetMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
       log.error(">>> ERROR 요청 감지: " + " method -> "+method + " URI ->" + uri);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }
	
}