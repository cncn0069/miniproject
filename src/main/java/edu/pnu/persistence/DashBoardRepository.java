package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.DashBoard;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long>{
	@Query("SELECT d FROM DashBoard d order by created_at desc")
	List<DashBoard> getDashBoardAll();
	
//	@Query("select new edu.pnu.dto.DashBoardDto(d.dashId, d.title, d.content, d.username, d.nickname, d.createdAt) from DashBoard d")
	@Query("select d from DashBoard d")
	List<DashBoard> getByIdAll();
}
