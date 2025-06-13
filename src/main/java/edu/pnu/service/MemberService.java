package edu.pnu.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.dto.MemberDto;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.util.JWTUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepo;

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
				.enabled(member.isEnabled())
				.build();
		
		return dto;
		
	}

}
