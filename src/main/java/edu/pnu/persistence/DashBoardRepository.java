package edu.pnu.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.DashBoard;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long>{
	@Query("SELECT d FROM DashBoard d where d.enabled = true order by createdAt desc")
	Page<DashBoard> getDashBoardAllEnabledNotFalse(Pageable pageable);
	
//	@Query("select new edu.pnu.dto.DashBoardDto(d.dashId, d.title, d.content, d.username, d.nickname, d.createdAt) from DashBoard d")
	@Query("SELECT d FROM DashBoard d where d.dash_id = :id and d.enabled = true")
	DashBoard getByIdEnabledNotFalse(Long id);
}
