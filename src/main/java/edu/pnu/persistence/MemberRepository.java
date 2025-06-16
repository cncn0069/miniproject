package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	
	@Query("select m.nickname from Member m")
	List<String> getAllNickname();
}
