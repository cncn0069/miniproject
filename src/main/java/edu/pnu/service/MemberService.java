package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.domain.UserRecentPages;
import edu.pnu.dto.MemberDto;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.persistence.UserRecentPagesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepo;
	
	@Autowired
	private UserRecentPagesRepository pagesRepo;

	public List<Member> getAllMembers(){
		return memberRepo.findAll();
	}
	public Member getMembers(String username) {
		return memberRepo.findById(username).get();
	}
	
	public MemberDto getMemberInfo(String name) {

		Member member = memberRepo.findById(name).orElseThrow(()->new UsernameNotFoundException("not exsited id"));
		
		MemberDto dto = MemberDto.builder()
				.nickname(member.getNickname())
				.username(member.getUsername())
				.created_at(member.getCreated_at())
				.address(member.getAddress())
				.enabled(member.isEnabled())
				.build();
		
		return dto;
		
	}
	//닉네임 불러오기
	public List<MemberDto> getNicknamesInfo(){
		List<String> members = memberRepo.getAllNickname();
		
		List<MemberDto> dtos = new ArrayList<>();
		
		for(String member: members) {
			dtos.add(MemberDto.builder()
					.nickname(member)
					.build());
		}
		return dtos;
	}
	
	public void setRecentPage(String username,Long id){
		//최근 방문 게시글 저장
		pagesRepo.save(UserRecentPages.builder()
				.username(username)
				.url("api/post/" + id)
				.visitedAt(LocalDateTime.now())
				.build());
	}
	
	public List<String> getRecentPages(String username) {
		return pagesRepo.getAllUrlByUsername(username);
	}

}
