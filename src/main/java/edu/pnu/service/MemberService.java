package edu.pnu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository mrp;
	
	public List<Member> getAllMembers(){
		return mrp.findAll();
	}
	public Member getMembers(String username) {
		return mrp.findById(username).get();
	}

}
