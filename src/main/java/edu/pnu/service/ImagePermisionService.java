package edu.pnu.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import edu.pnu.domain.Furniture;
import edu.pnu.dto.ApiResponseDTO;
import edu.pnu.dto.ImagePermitRequestDTO;
import edu.pnu.dto.ImagePermitResponseDTO;
import edu.pnu.dto.IndexedFurnitureList;
import edu.pnu.persistence.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagePermisionService {
	private final FurnitureRepository furnitureRepo; // @Autowired 제거
	private final WebClient fastApiWebClient;
	
	public Mono<ApiResponseDTO<ResponseEntity<List<IndexedFurnitureList>>>> postImagepermit(
	        String jobid,
	        ImagePermitRequestDTO imagePermitRequestDTO) {

	    if (imagePermitRequestDTO.getSelectedname() == null || imagePermitRequestDTO.getSelectedIdx() == null) {
	        log.info("imagePermitRequestDTO가 null");
	        return Mono.error(new IllegalArgumentException("Selected names or indices cannot be null"));
	    }

	    List<Integer> idxList = imagePermitRequestDTO.getSelectedIdx();
	    List<String> nameList = imagePermitRequestDTO.getSelectedname();

	    if (idxList.size() != nameList.size()) {
	        return Mono.error(new IllegalArgumentException("selectedIdx와 selectedname의 크기가 일치하지 않습니다."));
	    }

	    // 튜플 (index, itemName, type) 생성
	    List<Tuple3<Integer, String, String>> nameTypePairs = new ArrayList<>();

	    for (int i = 0; i < nameList.size(); i++) {
	        String[] parts = nameList.get(i).split("_", 2);
	        if (parts.length < 2) {
	            log.warn("형식 오류: {}", nameList.get(i));
	            return  Mono.just(
	            		ApiResponseDTO.<ResponseEntity<List<IndexedFurnitureList>>>builder()
		                .status("fail")
		                .message("작업물 가격 조회 실패")
		                .data(null)
		                .build()
	            		);
	        }
	        nameTypePairs.add(Tuples.of(idxList.get(i), parts[0], parts[1]));
	    }

	    // DB 조회 및 결과 반환
	    return Flux.fromIterable(nameTypePairs)
	        .flatMap(triple ->
	            Mono.fromCallable(() -> {
	                int idx = triple.getT1();
	                String item = triple.getT2();
	                String type = triple.getT3();
	                log.info("DB 조회: idx={}, item={}, type={}", idx, item, type);
	                Furniture list = furnitureRepo.getAllFurnitureByItemName(item, type);
	                return new IndexedFurnitureList(idx, list);
	            }).subscribeOn(Schedulers.boundedElastic())
	        )
	        .collectList()
	        .map(resultList ->
	            ApiResponseDTO.<ResponseEntity<List<IndexedFurnitureList>>>builder()
	                .status("200")
	                .message("작업물 가격 조회 성공")
	                .data(ResponseEntity.ok(resultList))
	                .build()
	        );
	}

	
	public Mono<ApiResponseDTO<ImagePermitResponseDTO>> permitImageMaksing(String jobid,ImagePermitRequestDTO imagePermitRequestDTO) {
		return fastApiWebClient.post()
	    .uri("/fastapi/inference/{jobid}/permission", jobid)
	    .contentType(MediaType.APPLICATION_JSON)
	    .bodyValue(imagePermitRequestDTO)
	    .retrieve()
	    .bodyToMono(new ParameterizedTypeReference<ApiResponseDTO<ImagePermitResponseDTO>>() {} )
	    .doOnSuccess(resp -> log.info("FastAPI에 승인 데이터 전송 완료"))
	    .doOnNext(res->log.info("FastAPI에 승인 데이터 전송 완료 로그:{}",res))
	    .doOnError(err -> log.error("FastAPI 전송 중 오류 발생", err));
	}
	
}

