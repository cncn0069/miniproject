package edu.pnu.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import edu.pnu.decoration.ObjectDeco;
import edu.pnu.domain.Furniture;
import edu.pnu.dto.ApiResponseDTO;
import edu.pnu.dto.ImagePermitRequestDTO;
import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;
import edu.pnu.persistence.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ImagePermisionService {
    private final WebClient fastApiWebClient;
    private final FurnitureRepository furnitureRepo; // @Autowired 제거

    public Mono<ApiResponseDTO<ResponseEntity<List<List<Furniture>>>>> postImagepermit(String jobid, ImagePermitRequestDTO imagePermitRequestDTO)  {
        // 반환 타입 수정
        if(imagePermitRequestDTO.getSelectedname() == null) {
            return Mono.error(new IllegalArgumentException("Selected names cannot be null"));
        }
        Set<String> set = new HashSet<>();
        
        for(String name: imagePermitRequestDTO.getSelectedname()) {
        	set.add(name);
        }
        // reactive 방식으로 개선
        return Flux.fromIterable(set)
                   .flatMap(name -> Mono.fromCallable(() -> furnitureRepo.getAllFurnitureByItemName(name)))
                   .collectList()
                   .map(furnitures -> ApiResponseDTO.<ResponseEntity<List<List<Furniture>>>>builder()
                        .status("200")
                        .message("작업물 가격")
                        .data(ResponseEntity.ok(furnitures))
                        .build());
    }
}
