package edu.pnu.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.core.context.SecurityContext;


import edu.pnu.dto.ApiResponseDTO;
import edu.pnu.dto.ImagePermitResultImageDTO;
import edu.pnu.dto.ImageProcessResultDTO;
import edu.pnu.dto.ImageUploadRequestDTO;
import edu.pnu.dto.ImageUploadResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
//추후 기능 많아지면 분리해야함
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessService {
	private final WebClient fastApiWebClient;
	/*
	//Netty 기반 WebFluX사용 tomcat이랑 같이 사용하면 안좋음 그냥해보는것

	@GetMapping("/imgToPython")
	public Mono<ResponseEntity<Map<String, String>>> getResizedImage(){
		return fastApiWebClient
				.get()
				.uri("/resize")
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
				.map(response -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON).body(response)
						);
	}
	
	//  서블릿 기반 RestTmeplet
	@Autowired
	private RestTemplate restTemplate;
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	@GetMapping("/resize-image")
	public ResponseEntity<byte[]> resizeImage() {
	    byte[] response = restTemplate.getForObject("http://localhost:8000/resize", byte[].class);
	    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response);
	}
	*/
	
	/*
	 * 주요 에러 //part)"image", file.getResource() → MultipartFile을 Spring 내부 Resource 타입으로 변환을 하면 
	 * MultipartHttpMessageWriter는 특정 타입만 multipart로 encode할 수 있음으로 에러
	 * String
	 * Resource (ByteArrayResource, InputStreamResource, FileSystemResource 등)
	 * Part (reactive multipart 사용 시)
	 * HttpEntity<?>
	 * MultipartBodyBuilder.build()로 생성된 Map<String, HttpEntity<?>>
	 * 해당 타입이 아닐경우 타입 변경이 필요함
	 * ByteArrayResource 파일을 byte[]로 메모리에 올린 후 ByteArrayResource로 감싸 전송하는 방식
	 * FileSystemResource 파일을 디스크에 저장한 후 해당 경로로 FileSystemResource를 생성해 전송
	 * InputStreamResource InputStream을 사용해 파일을 스트리밍 전송.
	 * RestTemplate Reactive가 아닌 Blocking 방식의 HTTP 클라이언트로 multipart 전송
	 * 4가지 모드 장단점이 있음
	 * */
	
	//이미지 전송
	public Mono<ResponseEntity<ApiResponseDTO<ImageUploadResponseDTO>>> sendImageToFastApi(
			ImageUploadRequestDTO imageUploadRequestDTO) throws IOException{
		
		log.info("FastAPI로 이미지 전송 시작 - username: {}, 파일명: {}, 크기: {} bytes, 타입: {}",
				imageUploadRequestDTO.getUsername(),
				imageUploadRequestDTO.getImage().getOriginalFilename(),
				imageUploadRequestDTO.getImage().getSize(),
				imageUploadRequestDTO.getImage().getContentType());
		//null 체크
		String contentType = Optional.ofNullable(imageUploadRequestDTO.getImage().getContentType())
                .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		//파일명 null체크 null일시 임시 UUID발급
		String filename = Optional.ofNullable(imageUploadRequestDTO.getImage().getOriginalFilename())
				.filter(name-> !name.isBlank())
				.orElse("upload-" + UUID.randomUUID());
		// image/jpeg, image/png 같은 실제 MIME 타입 미디어 타입 파씽
		MediaType mediaType = MediaType.parseMediaType(contentType);
		// 1. MultipartFile을 임시 파일로 저장
        File tempFile = File.createTempFile("upload-", "-" + filename);
        imageUploadRequestDTO.getImage().transferTo(tempFile);
        // 2. 디스크의 파일을 FileSystemResource로 감싸기
        FileSystemResource resource = new FileSystemResource(tempFile);
        
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", resource).filename(filename).contentType(mediaType);
        builder.part("username", imageUploadRequestDTO.getUsername());
        
		return fastApiWebClient.post()
				.uri("/fastapi/inference")
				.contentType(MediaType.MULTIPART_FORM_DATA)//요청 본문(Request Body)의 MIME 타입을 지정하는 부분
				//BodyInserters파일의 내용을 body에 붙임 MultipartBodyBuilder는 multipart 형식의 데이터를 만들기 위한 유틸리티
				.body(BodyInserters.fromMultipartData(builder.build()))
				/*WebClient의 비동기 방식에서 서버의 응답을 받는 
				 * 시점 실제로 FastAPI로부터 응답(Response)이 오면 이걸 처리 이 단계 이후부터는 응답 처리 로직*/
				.retrieve()
				/*여기서 부터 비동기 처리
				 * 응답 바디의 타입을 지정 Mono<Map<String, String>>는 비동기적으로 Map 형태의 데이터를 하나 받을 것 
				 * JSON을 반환할 경우 이를 처리할 수 있도록함
				 * ParameterizedTypeReference는 스프링 프레임워크에서 제공하는 클래스로, 
				 * 제네릭 타입을 갖는 객체의 타입 정보를 보존하기 위한 목적으로 사용된다. 
				 * 주로 제네릭 타입을 갖는 클래스나 메서드를 호출하고 그 결과를 가져올 때 사용된다.
				 * 일반적으로 자바의 제네릭은 런타임에 소거되기 때문에, 제네릭 타입에 대한 정보를 동적으로 추출하기 어렵다.
				 * 하지만 ParameterizedTypeReference를 사용하면 런타임에 제네릭 타입 정보를 유지할 수 있다.
				 */
				.bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<ImageUploadResponseDTO>>() {})
				/*응답을 Spring Web의 표준 ResponseEntity 형태로 감싸서 반환.
				 * 여기서 map()은 Mono의 결과가 오면 그걸 ResponseEntity로 변환*/
				
				.doOnNext(apiResponse->log.info("FastAPI 응답 Json: {} " ,apiResponse))
				.map(apiResponse -> ResponseEntity.ok(apiResponse))
	            .doFinally(signaltype->{
	            	boolean detected = tempFile.delete();
	            	if (detected) {
						log.info("임시 파일 삭제 성공 : "+ tempFile.getAbsolutePath());
					}else {
						log.warn("임시 파일 삭제 실패 : ", tempFile.getAbsolutePath());
					}
	            }).doOnError(error -> log.error("FastAPI 에러: {}", error.getMessage()));
	}
	

	public ResponseEntity<ApiResponseDTO<ImageUploadResponseDTO>> sendImageToFastApi2(
	        ImageUploadRequestDTO imageUploadRequestDTO, Authentication savedAuth, String token) throws IOException {

	    String contentType = Optional.ofNullable(imageUploadRequestDTO.getImage().getContentType())
	            .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
	    String filename = Optional.ofNullable(imageUploadRequestDTO.getImage().getOriginalFilename())
	            .filter(name -> !name.isBlank())
	            .orElse("upload-" + UUID.randomUUID());

	    MediaType mediaType = MediaType.parseMediaType(contentType);
	    File tempFile = File.createTempFile("upload-", "-" + filename);
	    imageUploadRequestDTO.getImage().transferTo(tempFile);
	    FileSystemResource resource = new FileSystemResource(tempFile);

	    MultipartBodyBuilder builder = new MultipartBodyBuilder();
	    builder.part("image", resource).filename(filename).contentType(mediaType);
	    builder.part("username", imageUploadRequestDTO.getUsername());

	    // ✅ SecurityContext 수동 설정
	    SecurityContext context = SecurityContextHolder.createEmptyContext();
	    context.setAuthentication(savedAuth);
	    SecurityContextHolder.setContext(context);

	    return fastApiWebClient.post()
	        .uri("/fastapi/inference")
	        .header("Authorization", token)
	        .contentType(MediaType.MULTIPART_FORM_DATA)
	        .body(BodyInserters.fromMultipartData(builder.build()))
	        .retrieve()
	        .bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<ImageUploadResponseDTO>>() {})
	        .doOnNext(apiResponse -> log.info("FastAPI 응답 JSON: {}", apiResponse))
	        .map(apiResponse -> ResponseEntity.ok(apiResponse))
	        .doOnError(error -> log.error("FastAPI 에러: {}", error.getMessage()))
	        .doFinally(signalType -> {
	            boolean deleted = tempFile.delete();
	            if (deleted) {
	                log.info("임시 파일 삭제 성공: {}", tempFile.getAbsolutePath());
	            } else {
	                log.warn("임시 파일 삭제 실패: {}", tempFile.getAbsolutePath());
	            }
	            // ✅ SecurityContext 정리
	            SecurityContextHolder.clearContext();
	        }).block();
	}
	

	public Mono<ResponseEntity<ApiResponseDTO<ImageUploadResponseDTO>>> sendImageToFastApi3(
	        ImageUploadRequestDTO imageUploadRequestDTO, String token) throws IOException {

		String filename = Optional.ofNullable(imageUploadRequestDTO.getImage().getOriginalFilename())
	            .filter(name -> !name.isBlank())
	            .orElse("upload-" + UUID.randomUUID());
	    File tempFile = File.createTempFile("upload-", "-" + filename);
	    
	    String contentType = Optional.ofNullable(imageUploadRequestDTO.getImage().getContentType())
	            .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
	    MediaType mediaType = MediaType.parseMediaType(contentType);
	    
		
	    imageUploadRequestDTO.getImage().transferTo(tempFile);
	    FileSystemResource resource = new FileSystemResource(tempFile);

	    MultipartBodyBuilder builder = new MultipartBodyBuilder();
	    builder.part("image", resource).filename(filename).contentType(mediaType);
	    builder.part("username", imageUploadRequestDTO.getUsername());
	    
	    // 현재 인증 정보 (컨트롤러에서 진입 시 보장)
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.isAuthenticated()) {
	        log.info("인증된 사용자: {}", auth.getName());
	    }

	    ExecutorService threadPool = Executors.newCachedThreadPool();
	    DelegatingSecurityContextExecutor securedExecutor = new DelegatingSecurityContextExecutor(threadPool);
	    Scheduler securedScheduler = Schedulers.fromExecutor(securedExecutor);

	    return fastApiWebClient.post()
	            .uri("/fastapi/inference")
	            .header("Authorization", token)
	            .contentType(MediaType.MULTIPART_FORM_DATA)
	            .bodyValue(builder.build())
	            .retrieve()
	            .bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<ImageUploadResponseDTO>>() {})
	            .subscribeOn(securedScheduler) 
	            .doOnNext(apiResponse -> log.info("FastAPI 응답 JSON: {}", apiResponse))
	            .map(apiResponse -> ResponseEntity.ok(apiResponse))
	            .doOnError(error -> log.error("FastAPI 에러: {}", error.getMessage()))
	            .doFinally(signalType -> {
	                boolean deleted = tempFile.delete();
	                if (deleted) {
	                    log.info("임시 파일 삭제 성공: {}", tempFile.getAbsolutePath());
	                } else {
	                    log.warn("임시 파일 삭제 실패: {}", tempFile.getAbsolutePath());
	                }
	            });
	}


	//처리결과 조회 폴링방식 택함 웹소켓, SSE, 콜백 URL, 메세지 큐 등이 있음 
	public Mono<ResponseEntity<ApiResponseDTO<ImageProcessResultDTO>>> getImageFromFastApi(String jobid) {
		return fastApiWebClient.get()
				.uri(uribuilder->uribuilder.path("/fastapi/inference/{jobid}/result").build(jobid))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<ImageProcessResultDTO>>() {})
				.map(response->ResponseEntity.ok().body(response))
				.doOnError(e->log.error("FastAPI 결과 조회 에러 {}",e.getMessage()))
				.onErrorResume(e->{
					ApiResponseDTO<ImageProcessResultDTO> fallback = ApiResponseDTO.<ImageProcessResultDTO>builder()
							.status("503")
							.message("FastApi 서버 응답 불가")
							.data(null)
							.build();
					return Mono.just(ResponseEntity.status(503).body(fallback));
				});
	}
	
	public ResponseEntity<ApiResponseDTO<ImagePermitResultImageDTO>> getImageFromFastApiFinalResult(String jobid) {
		try {
			ApiResponseDTO<ImagePermitResultImageDTO> response = fastApiWebClient.get()
				.uri(uribuilder -> uribuilder
					.path("/fastapi/inference/{jobid}/permission/result")
					.build(jobid))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<ImagePermitResultImageDTO>>() {})
				.block(); // 동기 처리

			log.info("파이썬 읽은 데이터: {}", response);

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log.error("FastAPI 최종 결과 조회 에러 {}", e.getMessage());

			ApiResponseDTO<ImagePermitResultImageDTO> fallback = ApiResponseDTO.<ImagePermitResultImageDTO>builder()
				.status("503")
				.message("FastApi 서버 응답 불가")
				.data(null)
				.build();

			return ResponseEntity.status(503).body(fallback);
		}
	}
	
	
}
