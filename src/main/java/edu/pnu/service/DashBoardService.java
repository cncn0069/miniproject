package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.DashBoard;
import edu.pnu.domain.Member;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.dto.DashResponseDto;
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
					.dashId(dto.getDashId())
					.username(member)
					.nickname(member.getNickname())
					.title(dto.getTitle())
					.content(dto.getContent())
					.enabled(true)
					.createdAt(LocalDateTime.now())
					.build());
		}
	public void insertDashBoard(DashBoardDto dto) throws AccessDeniedException{		
			if(dto.getDashId() != null)
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
					.createdAt(LocalDateTime.now())
					.enabled(true)
					.build());
			
		}	
	public DashResponseDto getDashBoards(int pageNum,int pageSize,String method,String q,String writer){
		
		Pageable pageable = null;		
		 if(method.equals("latest")) {
				pageable = PageRequest.of(pageNum-1, pageSize,
							Sort.by("createdAt").descending());
				
		}else if(method.equals("oldest")) {
				pageable = PageRequest.of(pageNum-1, pageSize,
						Sort.by("createdAt").ascending());
		}else if(method.equals("title")) {
			pageable = PageRequest.of(pageNum-1, pageSize,
					Sort.by("title").ascending());
		}else if(method.equals("nickname")) {
			pageable = PageRequest.of(pageNum-1, pageSize,
					Sort.by("nickname").ascending());
		}
		Page<DashBoard> page = null;
		if(q.equals("") && writer.equals("")) {
			page = dashBoardRepository.getDashBoardAllEnabledNotFalse(pageable);
		}else if(q.equals("") && !writer.equals("")){
			page = dashBoardRepository.getDashBoardByNickNameAndEnabledNotFalse(pageable, writer);
		}else if(!q.equals("") && writer.equals("")){
			page = dashBoardRepository.getDashBoardByQAndEnabledNotFalse(pageable,q);
		}else {
			page = dashBoardRepository.getDashBoardByQAndNickNameAndEnabledNotFalse(pageable, q, writer);
		}

		
		
		DashResponseDto dashResponseDto = DashResponseDto.builder()
					.dashboards(page.getContent().stream()
					.map(dashBoard -> DashBoardDto.builder()
							.dashId(dashBoard.getDashId())
							.title(dashBoard.getTitle())
							.content(dashBoard.getContent())
							.username(dashBoard.getUsername().getUsername())
							.nickname(dashBoard.getNickname())
							.createdAt(dashBoard.getCreatedAt())
							.enabled(true)
							.build())
					.collect(Collectors.toList()))
					.page(page.getNumber()+1)
					.size(page.getSize())
					.totalPages(page.getTotalPages())
					.totalElements(page.getTotalElements())
					.first(page.isFirst())
					.last(page.isLast())
					.build();
					
					
					
		
			return dashResponseDto;
		}
	public DashBoardDto getDashBoard(Long id) throws IllegalAccessError{
		
		DashBoard dashboards = dashBoardRepository.getByIdEnabledNotFalse(id);
		
		if(dashboards == null)
		{
			throw new IllegalAccessError("삭제된 게시글입니다");
		}
		
		return DashBoardDto.builder()
				.dashId(dashboards.getDashId())
				.title(dashboards.getTitle())
				.content(dashboards.getContent())
				.username(dashboards.getUsername().getUsername())
				.nickname(dashboards.getNickname())
				.createdAt(dashboards.getCreatedAt())
				.build();
		}
	
	public void deleteDashBoard(Long dashId) {
		
		DashBoard dashBoard = dashBoardRepository.findById(dashId).orElseThrow(()->new IllegalAccessError("없는 게시글입니다."));
		dashBoard.setEnabled(false);
		dashBoardRepository.save(dashBoard);
	}
	
	public List<String> getWritersInfo(){
		return dashBoardRepository.getNickNames();
	}
}
