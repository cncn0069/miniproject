package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.DashBoard;
import edu.pnu.domain.Member;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.persistence.DashBoardRepository;
import edu.pnu.persistence.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashBoardService {
	
	@Autowired
	DashBoardRepository dashBoardRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	public void uploadDashBoard(DashBoardDto dto) {
	
		Member member = memberRepository.findById(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

			dashBoardRepository.save(DashBoard.builder()
					.dash_id(dto.getDash_id())
					.username(member)
					.nickname(member.getNickname())
					.title(dto.getTitle())
					.content(dto.getContent())
					.created_at(LocalDateTime.now())
					.build());
		}
	public void insertDashBoard(DashBoardDto dto) throws AccessDeniedException{		
			if(dto.getDash_id() != null)
			{
				log.info("잘못된 요청입니다.");
				throw new AccessDeniedException("잘못된 접근입니다.");
			}

			
			Member member = memberRepository.findById(dto.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid username"));

			dashBoardRepository.save(DashBoard.builder()
					.username(member)
					.nickname(member.getNickname())
					.title(dto.getTitle())
					.content(dto.getContent())
					.created_at(LocalDateTime.now())
					.build());
			
		}

	
	public List<DashBoardDto> getDashBoards(){
		
		List<DashBoard> dashboards = dashBoardRepository.getByIdAll();
		
		List<DashBoardDto> dtos = new ArrayList<>();
		
		
		for(DashBoard dashBoard:dashboards) {
			dtos.add(DashBoardDto.builder()
						.dash_id(dashBoard.getDash_id())
						.content(dashBoard.getContent())
						.title(dashBoard.getTitle())
						.username(dashBoard.getUsername().getUsername())
						.nickname(dashBoard.getNickname())
						.createdAt(dashBoard.getCreated_at())
						.build());
						
		}
		
		//List<DashBoard> dashboards = dashBoardRepository.findAll();
		
		return dtos;
	}
	
	public DashBoardDto getDashBoard(Long id){
		
		DashBoard dashboards = dashBoardRepository.findById(id).get();
		
		 
		
		return DashBoardDto.builder()
				.dash_id(dashboards.getDash_id())
				.title(dashboards.getTitle())
				.content(dashboards.getContent())
				.username(dashboards.getUsername().getUsername())
				.nickname(dashboards.getNickname())
				.createdAt(dashboards.getCreated_at())
				.build();
	}
}
