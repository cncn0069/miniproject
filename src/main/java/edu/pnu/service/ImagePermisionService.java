package edu.pnu.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import edu.pnu.domain.Furniture;
import edu.pnu.dto.ApiResponseDTO;
import edu.pnu.dto.ImagePermitRequestDTO;
import edu.pnu.persistence.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagePermisionService {
	private final FurnitureRepository furnitureRepo; // @Autowired 제거
	private final WebClient fastApiWebClient;
	
	private void permitImageMaksing(String jobid,ImagePermitRequestDTO imagePermitRequestDTO) {
		fastApiWebClient.post()
	    .uri("/fastapi/inference/{jobid}/permission", jobid)
	    .contentType(MediaType.APPLICATION_JSON)
	    .bodyValue(imagePermitRequestDTO)
	    .retrieve()
	    .toBodilessEntity()
	    .doOnSuccess(resp -> log.info("FastAPI에 승인 데이터 전송 완료"))
	    .doOnNext(res->log.info("FastAPI에 승인 데이터 전송 완료 로그:{}",res))
	    .doOnError(err -> log.error("FastAPI 전송 중 오류 발생", err))
	    .subscribe();
	}
	
	public Mono<ApiResponseDTO<ResponseEntity<List<List<Furniture>>>>> postImagepermit(String jobid,
			ImagePermitRequestDTO imagePermitRequestDTO) {
		// 반환 타입 수정
		if (imagePermitRequestDTO.getSelectedname() == null) {
			log.info("imagePermitRequestDTO가 null");
	        return Mono.error(new IllegalArgumentException("Selected names and types cannot be null"));
	    }
		List<String> itemList = new ArrayList<>();
	    List<String> typeList = new ArrayList<>();
	    
		for (String name : imagePermitRequestDTO.getSelectedname()) {
			itemList.add(name.split("_")[0]);
			typeList.add(name.split("_")[1]);
		}
		
		log.info("요청 아이템 (변환) : {}",itemList);
		log.info("요청 아이템의 타입 : {}",typeList);
		
		List<Tuple2<String, String>> nameTypePairs = new ArrayList<>();
		
	    for (int i = 0; i < itemList.size(); i++) {
	        nameTypePairs.add(Tuples.of(itemList.get(i), typeList.get(i)));
	        log.info("페어로 만든 경우 : {}",nameTypePairs);
	        log.info("페어로 만든 경우 : {}",nameTypePairs.get(0).getT1());
	    }
	    
	    permitImageMaksing(jobid,imagePermitRequestDTO);
	    
//		 reactive 방식으로 개선
		return  Flux.fromIterable(nameTypePairs)
			    .flatMap((Tuple2<String, String> pair) ->
		        Mono.fromCallable(() ->
		            furnitureRepo.getAllFurnitureByItemName(pair.getT1(), pair.getT2())
		        	).subscribeOn(Schedulers.boundedElastic()))
			    .collectList()
			    .map(furnitureLists -> ApiResponseDTO.<ResponseEntity<List<List<Furniture>>>>builder()
			        .status("200")
			        .message("작업물 가격")
			        .data(ResponseEntity.ok(furnitureLists))
			        .build()
			    );

	}
	
	
}

