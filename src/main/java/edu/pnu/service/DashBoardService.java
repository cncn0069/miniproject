package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.DashBoard;
import edu.pnu.domain.Member;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.persistence.DashBoardRepository;
import edu.pnu.persistence.MemberRepository;

@Service
public class DashBoardService {
	
	@Autowired
	DashBoardRepository dashBoardRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	public void uploadDashBoard(DashBoardDto dto) {
		System.out.println(dto.getUsername());
		
		DashBoard dash = new DashBoard();
		Member member = memberRepository.findById(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        // 닉네임 검증 (옵션)
        if (!member.getNickname().equals(dto.getNickname())) {
            throw new IllegalArgumentException("닉네임 불일치");
        }
		
			dashBoardRepository.save(dash.builder()
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
	
	public DashBoard getDashBoard(Long id){
		
		DashBoard dashboards = dashBoardRepository.findById(id).get();
		
		return dashboards;
	}
}
