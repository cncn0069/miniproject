package edu.pnu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.dto.MemberDto;
import edu.pnu.persistence.MemberRepository;
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
	
	public void setMember(MemberDto dto) {
		
		Member member = memberRepo.getMemberByUsername(dto.getUsername());
		member.setNickname(dto.getNickname());
		member.setAddress(dto.getAddress());
		memberRepo.save(member);
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
	public void deleteMember(String username) {
		Member member = memberRepo.findById(username).orElseThrow(()->new UsernameNotFoundException("not exsited id"));
		member.setEnabled(false);
		memberRepo.save(member);
	}

}
