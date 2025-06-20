package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.UserRecentPage;

public interface UserRecentPageRepository extends JpaRepository<UserRecentPage, Long>{
	
	@Query(
			  value = "SELECT u.url FROM gbsorting.user_recent_pages u WHERE u.username LIKE :username ORDER BY u.created_at DESC LIMIT 5",
			  nativeQuery = true
			)
	List<String> getAllDashIdByUserName(String username);
}
