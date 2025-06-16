package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.UserRecentPages;

public interface UserRecentPagesRepository extends JpaRepository<UserRecentPages, Long>{
	
	@Query("select r.username from UserRecentPages r where r.username = :username")
	List<String> getAllUrlByUsername(String username);
}
